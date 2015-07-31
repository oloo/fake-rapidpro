# fake-rapidpro [![Build Status](https://travis-ci.org/oloo/fake-rapidpro.svg)](https://travis-ci.org/oloo/fake-rapidpro)

This is a Clojure web application that is used when testing an application that works with RapidPro.
Fake RapidPro would act as a test double which would behave exactly as RapidPro would in the case when calls to RapidPro
are not being mocked out. This would typically be the case when doing end to end tests

Version 1 of the RapidPro API found which is detailed at http://rapidpro.io/api/v1

# USAGE

To run the application, download the latest version of fake-rapidpro from  and execute the following command. This will start the webserver on port 3000

*Note that you will need JRE 6.x or later to have been installed

    $ java -jar fake-rapidpro.jar

## Configure RUN Responses

Before a run is started for a given flow and contact combination, the expected responses and webhook calls need  to be configured.

### Request Details

URL   http://<URL:PORT>/api/v1/flow-configs

METHOD: POST

HEADER:
```json
  {
    "Authorization": "Token SAMPLE_TOKEN",
    "Content-Type": "application/json"
    }
```

DATA:
```json
{
    "flow": "f5901b62-ba76-4003-9c62-72fdacc1b7b7",
    "responses": [{
                    "step" :     "23493-234234-23432-2342-3432",
                    "text" :    "Yes",
                    "webhook" : "http://localhost:3000/webhook"
                    },
                  {
                    "step" :     "23113-234234-23432-2342-112",
                    "text" :    55,
                    "webhook" : "http://localhost:3000/webhook"
                    }]
      }
```

RESPONSE STATUS: `201 OK`

## Start RUN

Currently starting a run for a contact by phone number is as follows and is exactly the same call as would be require for RapidPro:

### Request Details

URL   http://<URL:PORT>/api/v1/runs.json

METHOD: POST

HEADER:
```json
  {
    "Authorization": "Token SAMPLE_TOKEN",
    "Content-Type": "application/json"
    }
```

DATA:
```json
{
    "flow": "f5901b62-ba76-4003-9c62-72fdacc1b7b7",
    "phone": ["+256779500799"],
    "extra":
      {
        "Name":"John Doe"
      }
    }
  ```

RESPONSE STATUS: `201 OK`


# DEVELOPMENT

## Testing
To run all the tests, run the following command

    $ lein midje

To run the tests each time the application is updated, run the command

    $ lein midje :autotest

## Building

Install lein basing on the instructions on http://leiningen.org/#install

Run the following command:

    $ lein ring uberjar

## Starting the webserver

To run the application for development on port 3000

    $ lein ring server

## License

Distributed under the Eclipse Public License
