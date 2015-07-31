(ns fake-rapidpro.webhook-test
  (:require
    [fake-rapidpro.core :refer :all]
    [clj-http.client :as client])
  (:use midje.sweet)
  (:use clj-http.fake))

(facts "Testing Webhooks"
       (fact "Starting a run for a configured flow calls the webhooks"
             (with-fake-routes
               {
                "http://myapp.com/webhook-endpoint"
                (fn [_] {:status 201 :headers {} :body ""})
                }
               (let [response (call-webhook
                 "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                 {
                  :phone   "+256 779500794"
                  :step    "23493-234234-23432-2342-3432"
                  :text    "Yes"
                  :webhook "http://myapp.com/webhook-endpoint"
                  })]
                 (:status response) => 201)))
       (fact "The webhook is called with the right set of parameters"
             (call-webhook
               "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
               {
                :phone   "+256 779500794"
                :step    "23493-234234-23432-2342-3432"
                :text    "Yes"
                :webhook "http://myapp.com/webhook-endpoint"
                }) => nil
             (provided (client/post
                         "http://myapp.com/webhook-endpoint"
                         {:form-params
                           {
                            :run ["0"] :relayer ["0"]
                            :text "Yes" :flow "f5901b62-ba76-4003-9c62-72fdacc1b7b7"
                            :phone "+256 779500794" :step "23493-234234-23432-2342-3432"
                            :values [[{
                                       :category {
                                                  :eng "None"
                                                  }
                                       :time "2014-10-22T11:56:52.836354Z" :text "Yes"
                                       :rule_vale "Yes" :value "Yes" :label "No Label"
                                       }]]
                            :time ["2014-10-22T11:57:35.606372Z"]
                            }
                          :form-param-encoding "UTF-8"}) => nil :times 1)))