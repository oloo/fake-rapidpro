(ns fake-rapidpro.core
  (:use compojure.core
        ring.middleware.json)
  (:require [compojure.handler]
            [compojure.route]
            [ring.util.response :refer [response]]
            [clj-http.client :as client]))

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
    (and (flow-valid? flow)
         (config-responses-valid? responses))))

(defn run-request-valid?
  [run-request]
  (let [flow (get-in run-request [:flow])
        phone (get-in run-request [:phone])]
    (every? #(not (nil? %)) [flow (first phone)])))

(defn validate-run-post
  [request]
  (if (not (and (has-token? request)
                (run-request-valid?
                  (get-in request [:body]))))
    (throw (IllegalArgumentException. "Invalid run request"))))

(def ^:private flows (atom {}))

(defn flow
  [flow-uuid]
  (get @flows flow-uuid))

(defn clear-flows
  []
  (reset! flows {}))

(defn add-flow
  [flow-config]
  (if (flow-config-valid? flow-config)
    (swap! flows assoc (:flow flow-config) flow-config)
    (throw (IllegalArgumentException. "Wrong Configuration format"))))

(defn call-webhook
  [flow-id response]
  (println "Calling Webhook with: " response)
  (client/post (:webhook response)
               {:form-params
                                     {
                                      :run    ["0"] :relayer ["0"]
                                      :text   (:text response) :flow flow-id
                                      :phone  (:phone response) :step (:step response)
                                      :values [[{
                                                 :category  {
                                                             :eng "None"
                                                             }
                                                 :time      "2014-10-22T11:56:52.836354Z" :text "Yes"
                                                 :rule_vale "Yes" :value "Yes" :label "No Label"
                                                 }]]
                                      :time   ["2014-10-22T11:57:35.606372Z"]
                                      }
                :form-param-encoding "UTF-8"}))

(defn start-run
  [flow-id]
  (let [response-config (flow flow-id)]
    (if (nil? response-config)
      (throw (IllegalArgumentException. "Flow not configured"))
      ;'doall' is required to execute the map because it is lazy
        (doall (map #(call-webhook flow-id %)
            (:responses response-config))))))

(defn run-handler
  [request]
  (try
    (println "/api/v1/runs.json called with: " request)
    (validate-run-post request)
    (start-run (get-in request [:body :flow]))
    {:status 201}
    (catch IllegalArgumentException _
      {:status 400})))

(defn add-flow-handler
  [request]
  (try
    (println "/api/v1/flow-configs called with: " request)
    (add-flow (get-in request [:body]))
    {:status 201}
    (catch IllegalArgumentException _
      {:status 400})))

(defroutes app-routes
           (context "/" []
             (GET "/" []
               {:status 200
                :body   "RapidPro server is up"}))
           (context "/api/v1/runs.json" []
             (POST "/" [] run-handler))
           (context "/api/v1/flow-configs" []
             (POST "/" [] add-flow-handler)))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})))