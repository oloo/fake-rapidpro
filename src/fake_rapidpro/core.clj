(ns fake-rapidpro.core
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [response]]))

(defn- has-token?
  [request]
  (not (nil? (get-in request [:headers :Authorization]))))

(defn validate-payload
  [request]
  (if (has-token? request)
    {:status 201}
    {:status 400}))

(defroutes app-routes
           (context "/" []
             (GET "/" []
               {:status 200
                :body   "RapidPro server is up"}))
           (context "/api/v1/runs.json" []
             (POST "/" [] validate-payload)))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})))