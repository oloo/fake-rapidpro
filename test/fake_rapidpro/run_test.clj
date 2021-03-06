(ns fake-rapidpro.run-test
  (:require [fake-rapidpro.core :refer :all])
  (:use midje.sweet))

(facts "validate run request body"
       (fact "request is NOT valid if it does not have a flow id"
             (run-request-valid?
               {:phone ["+256779500794"]
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is NOT valid if it does not have a phone field"
             (run-request-valid?
               {:flow  "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is NOT valid if it does not have atleast one phone contact"
             (run-request-valid?
               {:flow  "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :phone []
                :extra {
                        :name "John Doe"
                        }}) => false)
       (fact "request is valid if it has both a flow uuid and atleast one phone number"
             (run-request-valid?
               {:flow  "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                :phone ["+256779500796"]
                :extra {
                        :name "John Doe"
                        }}) => true))

(background (before :facts (add-flow
                             {
                              :flow      "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                              :responses [{
                                           :step    "23493-234234-23432-2342-3432"
                                           :text    "Yes"
                                           :webhook "http://myapp.com/webhook-endpoint"
                                           }]
                              }))
            (after :facts (clear-flows)))

(facts "Starting runs"
       (fact "Valid run requests for a configured flow makes appropriate webhook calls"
             (start-run "f5901b62-ba76-4003-9c62-72fdacc1b7b7" "+256 779500791") => '(nil)
             (provided (call-webhook
                         "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                         "+256 779500791"
                         {
                          :step    "23493-234234-23432-2342-3432"
                          :text    "Yes"
                          :webhook "http://myapp.com/webhook-endpoint"
                          }) => nil
                       :times 1))

       (fact "INVALID run requests throws an Error"
             (start-run "wrong-flow-id" "+256 779500791")
             => (throws IllegalArgumentException "Flow not configured")))