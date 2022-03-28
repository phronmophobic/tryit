(ns com.phronemophobic.tryit
  (:refer-clojure :exclude [eval])
  (:import org.apache.commons.text.StringEscapeUtils))

(defn escape [^String s]
  (StringEscapeUtils/escapeXSI s))

(defn escape-command
  ([args]
   (clojure.string/join " "
                        (map escape args))))

(defn ->edn [o]
  (binding [*print-namespace-maps* false
            *print-length* false]
    (pr-str o)))

(defn escape-clojure-command
  [deps args]
  (escape-command
   (into ["clj" "-Sdeps" (binding [*print-namespace-maps* false]
                           (->edn
                            {:deps deps}))]
         args)))

(defn escape-clojure-main
  [deps ns]
  (escape-clojure-command deps
                          ["-M" "-m"
                           (if (instance? clojure.lang.Namespace ns)
                             (name (ns-name ns))
                             (name ns))]))


(defn escape-clojure-exec
  [deps f & args]
  (escape-clojure-command deps
                          (into
                           ["-X"
                            (->edn f)]
                           (map ->edn args))))

(defn escape-clojure-eval
  ([deps ns exprs]
   (escape-clojure-eval deps
                        (concat [(list 'require (list 'quote ns))
                                 (list 'ns ns)]
                                exprs)))
  ([deps exprs]
   (escape-clojure-command deps
                           ["-M" "-e"
                            (clojure.string/join
                             (map ->edn exprs))])))

(comment
  (println
   (escape-clojure-command '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                           ["-M" "-m" "com.phronemophobic.tryit"]))

  (println
   (escape-clojure-main '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                        'com.phronemophobic.tryit))

  (println
   (escape-clojure-exec '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                        'com.phronemophobic.tryit/exec))

  (println
   (escape-clojure-exec '{com.phronemophobic/tryit {:local/root "."}}
                        'com.phronemophobic.tryit/eval))

  (println
   (escape-clojure-eval '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                        'com.phronemophobic.tryit
                        '[(eval)]))
  ,
  )

(defn pf [s]
  (print s)
  (flush))

(defn -main [& args]
  (let [_ (pf "deps: " )
        deps (read)

        _ (pf "namespace: ")
        ns (read)]
    (println)
    (println (escape-clojure-main deps ns))
    (println)))


(defn exec [& args]
  (let [_ (pf "deps: " )
        deps (read)

        _ (pf "f: ")
        f (read)]
    (println)
    (println (escape-clojure-exec deps f))
    (println)))


(defn eval [& args]
  (let [_ (pf "deps: " )
        deps (read)

        _ (pf "ns: ")
        ns (read)

        _ (pf "form: ")
        form (read)]
    (println)
    (println (escape-clojure-eval deps ns [form]))
    (println)))


(defn print-help []
  (println
   "Tryit: Create cli snippets for quickly trying clojure libraries.

Usage:
  clojure -X:tryit :type type

:type should be one of eval, exec or main.
   :type eval - Create a command that evals expressions in a namespace
   :type exec - Create a command that invokes a function
   :type main - Create a command that invokes a namespace's main
"))

(defn cli [opts]
  (if (not (seq opts))
    (print-help)
    (case (:type opts)

      (:eval eval)
      (eval)

      (:exec exec)
      (exec)

      (:main main)
      (-main)

      ;; else
      (do
        (println "Invalid :type: " (pr-str (:type opts)))
        (print-help)))))
