(ns fake-rapidpro.core-test
  (:require [fake-rapidpro.core :refer :all])
  (:use midje.sweet))

(fact "This is a sample test that should pass"
   (and true true) => true)
