(ns fake-rapidpro.webhook-test
  (:require [fake-rapidpro.core :refer :all])
  (:use midje.sweet))

(facts "a well configured run for a configured flow calls the webhooks")