(ns fake-rapidpro.core
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler]
            [compojure.route]
            [ring.util.response :refer [response]]))

(defn- has-token?
  [request]
  (not (nil? (get-in request [:headers "authorization"]))))

(defn- flow-valid?
  [flow]
  (not (nil? flow)))

(defn- config-response-valid?
  [response]
  (every? true?
  (map #(contains? response %) [:phone :step :text :webhook])))

(defn- config-responses-valid?
  [responses]
  (and
    (not (empty? responses))
    (every? true? (map config-response-valid? responses))))

(defn flow-config-valid?
  [flow-config]
  (let [flow (get-in flow-config [:flow])
        responses (get-in flow-config [:responses])]
    (and (flow-valid? flow) (config-responses-valid? responses))))

(defn run-request-valid?
  [run-request]
  (let [flow (get-in run-request [:flow])
        phone (get-in run-request [:phone])]
    (every? #(not (nil? %)) [flow (first phone)])))

(defn validate-run-post
  [request]
  (if (and (has-token? request)
           (run-request-valid?
               (get-in request [:body])))
    {:status 201}
    {:status 400}))

(defn add-flow
  [flow-config]
  (if-not (flow-config-valid? flow-config)
    (throw (IllegalArgumentException. "Wrong Configuration format"))))

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