#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
This project was created from the archetype "wildfly-getting-started-archetype".

The project is configured to automatically provison and run an installation of WildFly
to host the application, no additional installation is needed.

To run the deploymnt in WildFly run the maven goals "package wildfly:run".


Testing:
This sample is prepared for running unit tests with the Arquillian framework.

The configuration can be found in "pom.xml":

Three profiles are defined:
-"default": no integration tests are executed.
-"arq-remote": you have to start a WildFly server on your machine. The tests are executed by deploying 
 the application to this server.
 Here the "maven-failsafe-plugin" is enabled so that integration tests can be run.
 Run maven with these arguments: "clean verify -Parq-remote"
-"arq-managed": this requires the environment variable "JBOSS_HOME" to be set: 
 The server found in this path is started and the tests are executed by deploying the application to this server.
 Instead of using this environment variable, you can also define the path in "arquillian.xml".
 Here the "maven-failsafe-plugin" is enabled so that integration tests can be run.
 Run maven with these arguments: "clean verify -Parq-managed"

The Arquillian test runner is configured with the file "src/test/resources/arquillian.xml" 
(duplicated in EJB and WEB project, depending where your tests are placed).
The profile "arq-remote" uses the container qualifier "remote" in this file.
The profile "arq-managed" uses the container qualifier "managed" in this file.

The project contains an integration test "SampleIT" which shows how to create the deployable WAR file using the ShrinkWrap API.
You can delete this test file if no tests are necessary.

Why integration tests instead of the "maven-surefire-plugin" testrunner?
The Arquillian test runner deploys the WAR file to the WildFly server and thus you have to build it yourself with the ShrinkWrap API.
The goal "verify" (which triggers the maven-surefire-plugin) is executed later in the maven build lifecyle than the "test" goal so that the target 
artifact ("login-form.war") is already built. You can build
the final WAR by including those files. The "maven-surefire-plugin" is executed before the WAR file
are created, so this WAR files would have to be built in the "@Deployment" method, too. 
