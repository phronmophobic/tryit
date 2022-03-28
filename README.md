# tryit

Create cli snippets for quickly trying clojure libraries.

![example usage](/example-usage.gif?raw=true)

## Try it!

### Create a command that invokes a function
```sh
clj -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.1\"}}} -X com.phronemophobic.tryit/exec
```

### Create a command that invokes a namespace's main
```sh
clj -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.1\"}}} -M -m com.phronemophobic.tryit
```

### Create a command that evals expressions in a namespace
```sh
clj -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.1\"}}} -M -e \(require\ \(quote\ com.phronemophobic.tryit\)\)\(ns\ com.phronemophobic.tryit\)\(eval\)
```

## Alias Usage

```clojure

{
 :aliases {
```
```clojure
           :tryit
           {:exec-fn com.phronemophobic.tryit/cli
            :replace-deps {com.phronemophobic/tryit {:mvn/version "1.1"}}}
```
```clojure
           }
}
```

```sh
$ clj -X:tryit
Tryit: Create cli snippets for quickly trying clojure libraries.

Usage:
  clj -X:tryit :type type

:type should be one of eval, exec or main.
   :type eval - Create a command that evals expressions in a namespace
   :type exec - Create a command that invokes a function
   :type main - Create a command that invokes a namespace's main

```

Example:

```sh
$ clj -X:tryit :type exec
deps: {com.phronemophobic/tryit {:mvn/version "1.1"}}
f: com.phronemophobic.tryit/cli

clj -Sdeps {:deps\ {com.phronemophobic/tryit\ {:mvn/version\ \"1.1\"}}} -X com.phronemophobic.tryit/cli

```

## Programmatic Usage

```clojure
(require '[com.phronemophobic.tryit :as tryit])

;; Three main functions that all return strings

;; Create an exec function command
(tryit/escape-clojure-exec '{com.phronemophobic/tryit {:mvn/version "1.1"}}
                           'com.phronemophobic.tryit/exec)

;; Create a main function command
(tryit/escape-clojure-main '{com.phronemophobic/tryit {:mvn/version "1.1"}}
                           'com.phronemophobic.tryit)

;; Create an eval function command
(tryit/escape-clojure-eval '{com.phronemophobic/tryit {:mvn/version "1.1"}}
                           'com.phronemophobic.tryit
                           '[(eval)])
```

## License

Copyright © 2022 Adrian

Distributed under the Eclipse Public License version 1.0.
