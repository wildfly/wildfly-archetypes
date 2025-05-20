This project was created from the archetype "wildfly-jakartaee-ear-archetype".

To deploy it:
Run the maven goals "install wildfly:deploy"

To undeploy it:
Run the maven goals "wildfly:undeploy"

==========================

DataSource:
This sample includes a "persistence.xml" file in the EJB project. This file defines
a persistence unit "${rootArtifactId}PersistenceUnit" which uses the JakartaEE default database.

In production environment, you should define a database in WildFly config and point to this database
in "persistence.xml".

If you change the datasource name and use provisioning (profile "provision" or "arq-provisioned", see below),
you have to update the provisioning config.

If you don't use entity beans, you can delete "persistence.xml".
==========================

Jakarta Faces:
The web application is prepared for Jakarta Faces 4.0 by bundling an empty "faces-config.xml" in "src/main/webapp/WEB-INF".
In case you don't want to use Jakarta Faces, simply delete this file and "src/main/webapp/beans.xml".
==========================

Provisioning:
The project defines two profiles "provision" and "arq-provisioned" that create a provisioned WildFly server (in "target/server" or "target/server_arquillian")
by auto discovering features necessary to run this application.

The profile "provision" is meant to build a server that contains this enterprise application as deployment.
The profile "arq-provisioned" also builds a server, then runs the arquillian tests. This profile does not add the deployment, so that the arquillian
tests can deploy the app themselves enriched with additional test classes.

As "persistence.xml" uses the datasource "java:comp/DefaultDataSource", Glow must be configured to create
the datasource in the WildFly server config. Currently, the default H2 datasource is configured:

    <add-ons>
        <add-on>h2-database:default</add-on>
    </add-ons>

If you change the datasource name in "persistence.xml" or want to use a different datasource, you have to change the Glow configuration
so that it creates the necessary snippets in the WildFly config.

When using a provisioned server, you would probably deploy the app as root application, as the server provides only one application -
no need to have a context path.
You could achieve this by changing the configuration of the "maven-ear-plugin" in "ear/pom.xml", see
https://maven.apache.org/plugins/maven-ear-plugin/examples/customizing-context-root.html
This will register the web application to the context root.
==========================

Testing:
This sample is prepared for running JUnit5 unit tests with the Arquillian framework.

The configuration can be found in "${rootArtifactId}/pom.xml":

The test code is contained in a separate module "integration_tests". Reason: the Arquillian tests
pick the ear file, add additional server side test classes and deploy this modified ear. By
placing the tests in their own module, they are run after the ear project was built and thus
can grab the resulting ear file.

Five profiles are defined:
-"default" and "provision": no integration tests are executed.
-"arq-remote": you have to start a WildFly server on your machine. The tests are executed by deploying
 the application to this server.
 Here the "maven-failsafe-plugin" is enabled so that integration tests can be run.
 Run maven with these arguments: "clean verify -Parq-remote"
-"arq-managed": this requires the environment variable "JBOSS_HOME" to be set:
 The server found in this path is started and the tests are executed by deploying the application to this server.
 Instead of using this environment variable, you can also define the path in "arquillian.xml".
 Here the "maven-failsafe-plugin" is enabled so that integration tests can be run.
 Run maven with these arguments: "clean verify -Parq-managed"
 -"arq-provisioned": the tests are executed by deploying the application to the
 server that is created during the provisioning step (in "target/server").

The Arquillian test runner is configured with the file "integration-tests/src/test/resources/arquillian.xml".
The profile "arq-remote" uses the container qualifier "remote" in this file.
The profile "arq-managed" uses the container qualifier "managed" in this file.
The profile "arq-provisioned" uses the container qualifier "provisioned" in this file, which sets
JBOSS_HOME to "../ear/target/server_arquillian" (note the relative path, as we have to move from the "integration_tests"
project to the "ear" project, where the provisioned server is placed).

Unit tests can be added to the "integration-tests" module, the EJB project and/or to the Web project.

The "integration-tests" project contains an integration test "SampleIT" which shows how to create the deployable EAR file using the ShrinkWrap API.
You can delete this test file if no tests are necessary.

Why integration tests instead of the "maven-surefire-plugin" testrunner?
This is not actually required here, as the Arquillian tests are placed in their own module. 
But if you want to add server side Arquillian tests also to the web module or the ejb module (which would mean that you deploy the web application or ejb jar as 
a standalone module), it is necessary to execute those tests as integration tests. The Arquillian test runner deploys the WAR or EJB jar file to the 
WildFly server and thus you have to build it yourself with the help of the ShrinkWrap API.
The goal "verify" (which triggers the maven-surefire-plugin) is executed later in the maven build lifecyle than the "test" goal so that the target
artifacts ("${rootArtifactId}-ejb.jar" and "${rootArtifactId}-web.war") are already built. You can build
the final WAR/JAR by adding test classes to those files. The "maven-surefire-plugin" is executed before the JAR/WAR files
are created, so those JAR/WAR files would have to be built in the "@Deployment" method, too.
