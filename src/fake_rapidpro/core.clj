(ns fake-rapidpro.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]))

(defn validate-params
  [ctx]
  (if (= "rapidpro" (get-in ctx [:request :params "type"]))
      ctx))

(defroutes app
  (ANY "/" []
    (resource
      :available-media-types ["text/html"]
      :handle-ok "RapidPro server is up"))
  (ANY "/api" []
    (resource
      :available-media-types ["text/html"]
      :exists? validate-params
      :handle-ok "Correct parameter value"
      :handle-not-found "Wrong value for parameter or param not specified")))

(def handler
  (-> app
      wrap-params))
