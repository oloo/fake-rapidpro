(ns fake-rapidpro.core
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [response]]))

(defn- has-token?
  [request]
  (not (nil? (get-in request [:headers "authorization"]))))

(defn valid-run-request?
  [body]
  (let [flow (get-in body [:flow])
        phone (get-in body [:phone])]
    (every? #(not (nil? %)) [flow (first phone)])))

(defn validate-run-post
  [request]
  (if (and (has-token? request)
           (valid-run-request?
               (get-in request [:body])))
    {:status 201}
    {:status 400}))

(defroutes app-routes
           (context "/" []
             (GET "/" []
               {:status 200
                :body   "RapidPro server is up"}))
           (context "/api/v1/runs.json" []
             (POST "/" [] validate-run-post)))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})))