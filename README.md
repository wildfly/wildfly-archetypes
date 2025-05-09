WildFly archetypes
==================

This project contains Maven Archetypes to generate Maven projects to develop Jakarta EE applications with [WildFly](https://wildfly.org/)

* [wildfly-jakartaee-webapp-archetype](/wildfly-jakartaee-webapp-archetype/) generates a Maven project to develop a simple Web Archive (WAR) with WildFly
* [wildfly-jakartaee-ear-archetype](/wildfly-jakartaee-ear-archetype/) generates a Maven project to develop a Entreprise Archive (EAR) with WildFly. It generates an EJB and WAR modules to compose the EAR
* [wildfly-subsystem-archetype](/wildfly-subsystem-archetype/) generates a Maven project to develop a WildFly subsystem to extend the capabilities of WildFly.

## Component dependencies

The versions of all dependencies and plugins that are used by this archetype are configured in the parent's `pom.xml`.

To update the archetypes to new versions:

* update to latest "org.jboss:jboss-parent" version found at https://repo.maven.apache.org/maven2/org/jboss/jboss-parent/
* update the version property named "version.wildfly.bom"
* update the version property named "version.wildfly.core" to the version bundled with WildFly (found in "%WILDFLY_HOME%/modules/system/layers/base/org/jboss/as/controller/main").
* check whether dependencies have changed.
* check the plugin versions and update if necessary:
  * wildfly-maven-plugin: https://repo.maven.apache.org/maven2/org/wildfly/plugins/wildfly-maven-plugin/
  * maven-compiler-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/
  * maven-surefire-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-surefire-plugin/
  * maven-failsafe-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-failsafe-plugin/
  * maven-war-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-war-plugin/
  * maven-ear-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-ear-plugin/
  * maven-ejb-plugin: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-ejb-plugin/
* update JUnit dependencies:
  * JUnit 4 (subsystem archetype): https://repo.maven.apache.org/maven2/junit/junit/
  * JUnit 5 (other archetypes): https://repo.maven.apache.org/maven2/org/junit/junit-bom/

### Injection of dependencies in the archetypes resources

The versions of all dependencies and plugins that are used by this archetype are configured in the parent's `pom.xml`.

For each archetype, their archetype-resources `pom.xml` is still in Git but in a separate source tree (e.g. `wildfly-jakartaee-ear-archetype/src/main/resources-filtered/archetype-resources/pom.xml`).
When the archetypes are built, their `pom.xml` are filtered with the property values from the parent pom (so their own `version.wildfly.bom` will have the *actual* value of the parent's `version.wildfly.bom`).

The reason of this structure is that we want to evaluate some property values when we built the archetype (for the versions) and keep others that will be evaluate when the project is generated by the archetypes (eg `${rootArtifactId}`) . The latter properties are escaped from filtering by prepending a `\`.