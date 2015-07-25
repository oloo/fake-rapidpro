(ns fake-rapidpro.run-test
(:require [fake-rapidpro.core :refer :all])
(:use midje.sweet))

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


