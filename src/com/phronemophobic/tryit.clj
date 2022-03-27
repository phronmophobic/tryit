(ns com.phronemophobic.tryit
  (:refer-clojure :exclude [eval])
  (:import org.apache.commons.text.StringEscapeUtils))

(defn escape [^String s]
  (StringEscapeUtils/escapeXSI s))

(defn escape-command
  ([args]
   (clojure.string/join " "
                        (map escape args))
   ))

(defn escape-clojure-command
  [deps args]
  (escape-command
   (into ["clojure" "-Sdeps" (pr-str
                              {:deps deps})]
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
                            (pr-str f)]
                           (map pr-str args))))

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
                             (map pr-str exprs))])))

(comment
  (println
   (escape-clojure-command '{com.phronemophobic/tryit {:local/root "."}}
                           ["-M" "-m" "com.phronemophobic.tryit"]))


  (println
   (escape-clojure-exec '{com.phronemophobic/tryit {:local/root "."}}
                        'com.phronemophobic.tryit/exec))

  (println
   (escape-clojure-exec '{com.phronemophobic/tryit {:local/root "."}}
                        'com.phronemophobic.tryit/eval))

  (println
   (escape-clojure-eval '{com.phronemophobic/tryit {:local/root "."}}
                        'com.phronemophobic.tryit
                        '[(println *ns*)
                          (+ 1 2)]))
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
    (println (escape-clojure-main deps ns))))


(defn exec [& args]
  (let [_ (pf "deps: " )
        deps (read)

        _ (pf "f: ")
        f (read)]
    (println (escape-clojure-exec deps f))))


(defn eval [& args]
  (let [_ (pf "deps: " )
        deps (read)

        _ (pf "ns: ")
        ns (read)

        _ (pf "form: ")
        form (read)]
    (println (escape-clojure-eval deps ns [form]))))


