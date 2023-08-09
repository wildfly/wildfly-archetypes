#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
This project was created from the archetype "wildfly-getting-started-archetype".

The project is configured to automatically provison and run an installation of WildFly
to host the application, no additional installation is needed.

To create a WildFly server and deploy the application in it, run `mvn clean package`
To start the WildFlyServer, run `./target/server/bin/standalone.sh`

Testing:
This sample is prepared for running unit tests with the Arquillian framework.

The configuration can be found in "pom.xml":

Integration tests are executed against the provisioned WildFly server with the application deployed
in it.

The project contains an integration test "SampleIT" which shows how to test your application.
You can delete this test file if no tests are necessary.

To run the integration tests, run `mvn clean package verify`