(ns fake-rapidpro.core-test
  (:require [fake-rapidpro.core :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json])
  (:use midje.sweet))

(facts "Server tests"
       (fact "Calling the root of the app returns server Status"
             (let [response (app (mock/request :get "/"))]
               (:status response) => 200
               (:body response) => "RapidPro server is up"))

       (fact "Starting a run for a contact without a token fails"
             (let [response (app (mock/request :post "/api/v1/runs.json"))]
               (:status response) => 400))

       (fact "Starting a run for a contact with a token and the right request params returns OK"
             (let [response (app
                              (->
                                (mock/request :post "/api/v1/runs.json")
                                (mock/header :Authorization "Token SampleToken")
                                (mock/content-type "application/json")
                                (mock/body (json/generate-string
                                             {:flow  "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                                              :phone ["+256779500796"]
                                              :extra {
                                                      :name "John Doe"
                                                      }}))
                                ))]
               (:status response) => 201)))