# tryit

Create cli snippets for quickly trying clojure libraries.

## Try it!

### Create a command that invokes a function
```sh
clojure -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.0\"}}} -X com.phronemophobic.tryit/exec
```

### Create a command that invokes a namespace's main
```sh
clojure -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.0\"}}} -M -m com.phronemophobic.tryit
```

### Create a command that evals expressions in a namespace
```sh
clojure -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.0\"}}} -M -e \(require\ \(quote\ com.phronemophobic.tryit\)\)\(ns\ com.phronemophobic.tryit\)\(eval\)
```

## Alias Usage

```clojure

{
 :aliases {
```
```clojure
           :tryit
           {:exec-fn com.phronemophobic.tryit/cli
            :replace-deps {com.phronemophobic/tryit {:mvn/version "1.0"}}}
```
```clojure
           }
}
```

$ clojure -X:tryit :type type

:type should be one of eval, exec or main.
   :type eval - Create a command that evals expressions in a namespace
   :type exec - Create a command that invokes a function
   :type main - Create a command that invokes a namespace's main

Example:

```sh
$ clojure -X:tryit :type exec
deps: {com.phronemophobic/tryit {:mvn/version "1.0"}}
f: com.phronemophobic/cli

clojure -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.0\"}}} -X com.phronemophobic/cli

```

## Programmatic Usage

```clojure
(require '[com.phronemophobic.tryit :as tryit])

;; Three main functions that all return strings

;; Create an exec function command
(tryit/escape-clojure-exec '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                           'com.phronemophobic.tryit/exec)

;; Create a main function command
(tryit/escape-clojure-main '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                           'com.phronemophobic.tryit)

;; Create an eval function command
(tryit/escape-clojure-eval '{com.phronemophobic/tryit {:mvn/version "1.0"}}
                           'com.phronemophobic.tryit
                           '[(eval)])
```

## License

Copyright © 2022 Adrian

Distributed under the Eclipse Public License version 1.0.
