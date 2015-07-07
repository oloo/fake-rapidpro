# fake-rapidpro [![Build Status](https://travis-ci.org/oloo/fake-rapidpro.svg)](https://travis-ci.org/oloo/fake-rapidpro)

This is a Clojure application that is used when testing an application that works with RapidPro.
Fake RapidPro would act as a test double which would behave exactly as RapidPro would in the case when calls to RapidPro
are not being mocked out. This would typically be the case when doing end to end tests

Version 1 of the RapidPro API found which is detailed at http://rapidpro.io/api/v1

Currently starting a flow for a single contact is as follows:

### Request

URL: http://localhost:3000/api/v1/runs.json

METHOD: POST

HEADER:
  {
    "Authorization": "Token SAMPLE_TOKEN",
    "Content-Type": "application/json"
    }

DATA:
  {
    "flow_uuid": "f5901b62-ba76-4003-9c62-72fdacc1b7b7",
    "groups": ["b775ea51-b847-4a20-b668-6c4ce2f61356"]
    "contacts": ["09d23a05-47fe-11e4-bfe9-b8f6b119e9ab", "f23777a3-e606-41d8-a925-3d87370e1a2b"],
    "restart_participants": true,
    "extra":
      {
        "Name":"John Doe"
      }
    }
**Note that the port can change depending on the configuration passed while starting up the server**

### Response

STATUS: 200 OK

## Testing
To run all the tests, run the following command

    $ lein midje

To run the tests each time the application is updated, run the command

    $ lein midje :autotest

## Building

Install lein basing on the instructions on http://leiningen.org/#install

Run the following command:

    $ lein ring uberjar

## Usage

To run the application for development on port 3000

    $ lein ring server

To run the application, run the  following command. Keep in mind that for now args are optional. Note that you will need JDK 6.x installed as a prerequisite

    $ java -jar fake-rapidpro-0.1.0.jar [args]

## License

Distributed under the Eclipse Public License
