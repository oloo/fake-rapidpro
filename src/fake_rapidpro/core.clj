(ns fake-rapidpro.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]))

(defn validate-params
  [ctx]
  (if (= "rapidpro" (get-in ctx [:request :params "type"]))
      ctx))

(defn handle-post
  [ctx]
  (let [header (get-in ctx [:request :header])
        body (get-in ctx [:request :body])]
        {:header header :body body}))

(defroutes app
  (ANY "/" []
    (resource
      :available-media-types ["text/html"]
      :handle-ok "RapidPro server is up"))
  (ANY "/api/v1/runs.json" []
    (resource
      :allowed-methods [:post]
      :available-media-types ["application/json"]
      :exists? validate-params
      :handle-ok "OK"
      :handle-not-found "Wrong value for parameter or param not specified"
      :post! handle-post)))

(def handler
  (-> app
      wrap-params))
