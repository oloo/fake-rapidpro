(defproject fake-rapidpro "0.1.0"
  :description "Fake RapidPro API"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.3.4"]
                 [cheshire "5.5.0"]]
  :plugins [[lein-ring "0.8.11" :exclusions [org.clojure/clojure]]
            [lein-midje "3.1.3" :exclusions [leiningen-core]]]
  :ring {:handler fake-rapidpro.core/app
          :nrepl {:start? true :port 7000}}
  :profiles {:dev {:dependencies [[midje "1.6.3" :exclusions [org.clojure/clojure]]
                                  [javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.2.0"]]}})
