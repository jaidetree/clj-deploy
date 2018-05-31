(defproject clj-deploy "0.1.0"
  :description "A simple library to post generic deploy instructions to various webservers."
  :url "https://github.com/jayzawrotny/clj-deploy"
  :license {:name "BSD 3 Clause"
            :url "https://github.com/jayzawrotny/clj-deploy/LICENSE"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-sign "0.1.1"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]]
  :deploy-repositories [["releases" :clojars]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
