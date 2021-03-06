(ns fake-rapidpro.flow-config-test
  (:require [fake-rapidpro.core :refer :all])
  (:use midje.sweet))

(facts "test setup for flows and responses"
       (fact "a flow configuration is NOT valid without a flow id"
             (flow-config-valid? {
                                  :responses [{}]
                                  }) => false)
       (fact "a flow configuration in NOT valid without a list of responses"
             (flow-config-valid? {
                                  :flow "23493-234234-23432-2342-3432"
                                  }) => false)
       (fact "response is NOT valid if it does not have step, text and webhook"
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :text "Yes"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :step  "23493-234234-23432-2342-3432"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :text    "Yes"
                                               :webhook "http://localhost:3000/webhook"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :step "23493-234234-23432-2342-3432"
                                               :text "Yes"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :step    "23493-234234-23432-2342-3432"
                                               :webhook "http://localhost:3000/webhook"
                                               }]
                                  }) => false)
       (fact "a flow configuration is valid without a flow id, and all responses with phone, text, step and webhook"
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :step    "23493-234234-23432-2342-3432"
                                               :text    "Yes"
                                               :webhook "http://localhost:3000/webhook"
                                               }]
                                  }) => true))

(background (before :facts (clear-flows))
            (after :facts (clear-flows)))

(facts "valid flow configurations are saved"
       (fact "an invalid flow throws an Error"
             (add-flow {}) => (throws IllegalArgumentException "Wrong Configuration format")
             (provided (flow-config-valid? {}) => false :times 1))

       (fact "an INVALID flow is NOT added to the configured flows"
             (add-flow {
                        :flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                        }) =>
             (throws IllegalArgumentException "Wrong Configuration format")

             (flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7") => nil)

       (fact "a valid flow is added to the configured flows"
             (add-flow {
                        :flow      "23493-234234-23432-2342-3432"
                        :responses [{
                                     :step    "23493-234234-23432-2342-3432"
                                     :text    "Yes"
                                     :webhook "http://localhost:3000/webhook"
                                     }]
                        })

             (flow "23493-234234-23432-2342-3432") =>
             {
              :flow      "23493-234234-23432-2342-3432"
              :responses [{
                           :step    "23493-234234-23432-2342-3432"
                           :text    "Yes"
                           :webhook "http://localhost:3000/webhook"
                           }]
              }))