(ns fake-rapidpro.core-test
  (:require [fake-rapidpro.core :refer :all]
            [ring.mock.request :as mock])
  (:use midje.sweet))

(fact "Calling the root of the app returns server Status"
      (let [response (app (mock/request :get "/"))]
        (:status response) => 200
        (:body response) => "RapidPro server is up"))

(fact "Starting a run for a contact without a token fails"
      (let [response (app (mock/request :post "/api/v1/runs.json"))]
        (:status response) => 400))

(fact "Starting a run for a contact with a token returns OK"
      (let [response (app
                       (mock/request
                         :post "/api/v1/runs.json"
                         {:content-type "application/json"
                          :body         "{ \"flow_uuid\" : 123}"}))]
        (:status response) => 201))

