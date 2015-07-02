(defproject fake-rapidpro "0.1.0"
  :description "Fake RapidPro API"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.13"]
                 [compojure "1.3.4"]
                 [ring/ring-core "1.2.1"]]
  :plugins [[lein-ring "0.8.11" :exclusions [org.clojure/clojure]]
            [lein-midje "3.1.3" :exclusions [leiningen-core]]]
  :ring {:handler fake-rapidpro.core/handler}
  :profiles {:dev {:dependencies [[midje "1.6.3" :exclusions [org.clojure/clojure]]]}})
