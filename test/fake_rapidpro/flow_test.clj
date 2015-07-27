(ns fake-rapidpro.flow-test
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
       (fact "response is NOT valid if it does not have phone, step, text and webhook"
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :phone "+256 779500794"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :phone "+256 779500794"
                                               :step  "23493-234234-23432-2342-3432"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :phone "+256 779500794"
                                               :text  "Yes"
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
                                               :phone "+256 779500794"
                                               :step  "23493-234234-23432-2342-3432"
                                               :text  "Yes"
                                               }]
                                  }) => false
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :phone   "+256 779500794"
                                               :step    "23493-234234-23432-2342-3432"
                                               :webhook "http://localhost:3000/webhook"
                                               }]
                                  }) => false)
       (fact "a flow configuration is valid without a flow id, and all responses with phone, text, step and webhook"
             (flow-config-valid? {
                                  :flow      "23493-234234-23432-2342-3432"
                                  :responses [{
                                               :phone   "+256 779500794"
                                               :step    "23493-234234-23432-2342-3432"
                                               :text    "Yes"
                                               :webhook "http://localhost:3000/webhook"
                                               }]
                                  }) => true))

(facts "valid flow configurations are saved"
       (fact "an invalid flow throws an Error"
             (add-flow {}) => (throws IllegalArgumentException "Wrong Configuration format")
             (provided (flow-config-valid? {}) => false)

             (add-flow {}) => nil
             (provided (flow-config-valid? {}) => true)
             ))