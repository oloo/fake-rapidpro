# fake-rapidpro

This is a clojure application that is used when testing an application that works with RapidPro.
Fake RapidPro would act as a test double which would behave exactly as RapidPro would in the case when calls to RapidPro
are not being mocked out. This would typically be the case when doing end to end tests

## Testing
To run all the tests, run the following command

    $ lein midje

## Building

Install lein
Run the following command:

    $ lein uberjar

## Usage

To run the application, run the  following command. Keep in mind that for now args are optional

    $ java -jar fake-rapidpro-0.1.0.jar [args]

## License

Distributed under the Eclipse Public License
