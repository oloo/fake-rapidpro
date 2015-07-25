(ns fake-rapidpro.core-test
  (:require [fake-rapidpro.core :refer :all]
            [ring.mock.request :as mock])
  (:use midje.sweet))
(facts "Server tests"
       (fact "Calling the root of the app returns server Status"
             (let [response (app (mock/request :get "/"))]
               (:status response) => 200
               (:body response) => "RapidPro server is up"))

       (fact "Starting a run for a contact without a token fails"
             (let [response (app (mock/request :post "/api/v1/runs.json"))]
               (:status response) => 400))

       (fact "Starting a run for a contact with a token returns OK"
             (let [response (app
                              (->
                                (mock/request
                                  :post "/api/v1/runs.json")
                                (mock/header :Authorization "Token SampleToken")))]
               (:status response) => 201)))

(facts "validate request body"
       (fact "request is NOT valid if it does not have a flow id"
             (valid-request?
               {:phone ["+256779500794"]
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is NOT valid if it does not have a phone field"
             (valid-request?
               {:flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is NOT valid if it does not have atleast one phone contact"
             (valid-request?
               {:flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :phone []
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is valid if it has both a flow uuid and atleast one phone number"
             (valid-request?
               {:flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :phone ["+256779500796"]
                :extra {
                        :name "John Doe"
                        }}) => true))

