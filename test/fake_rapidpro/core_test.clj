(ns fake-rapidpro.core-test
  (:require [fake-rapidpro.core :refer :all]
            [ring.mock.request :as mock])
  (:use midje.sweet))

(fact "Calling the root of the app returns server Status"
  (let [response (app (mock/request :get "/"))]
    (:status response) => 200
    (:body response) => "RapidPro server is up"))

(fact "Starting a run for a contact returns an OK response"
  (let [response (app (mock/request :post "/api/v1/runs.json"))]
    (:status response) => 201))
