# ${groupId}:${artifactId} extension for WildFly

This project was created from the archetype "wildfly-subsystem".

It is composed of 3 Maven modules:

* ${artifactId}-extension
  * it contains the Java code that is installed in WildFly to add the extension and its subsystems.
* ${artifactId}-feature-pack
  * it creates a Feature Pack to provision the extension and install it in a WildFly installation
* ${artifactId}-testsuite
  * it provisions a WildFly installation with the feature pack and test that its features are working as expected

# Build the project

Execute the command:

```
mvn install
```

# Extension documentation

When the feature pack is built, it describes its content (including the layer and subsystem that it provides)
in a browsable [documentation](${artifactId}-feature-pack/target/doc/index.html).

# Run the WildFly server

Once the project has been built, you can run a WildFly server with the extension installed by executing the command:

```
./${artifactId}-testsuite/target/server/bin/standalone.sh
```

# Provision this extension with WildFly

To install this extension in your own server, you must provision its feature pack as shown in the ${artifactId}-testsuite/pom.xml:

```
            <plugin>
                <groupId>org.wildfly.plugins</groupId>
                <artifactId>wildfly-maven-plugin</artifactId>
                <configuration>
                    <feature-packs>
                        <feature-pack>
                            <groupId>org.wildfly</groupId>
                            <artifactId>wildfly-galleon-pack</artifactId>
                            <version>${version.wildfly}</version>
                        </feature-pack>
                        <feature-pack>
                            <groupId>${groupId}</groupId>
                            <artifactId>${artifactId}-feature-pack</artifactId>
                            <version>${version}</version>
                        </feature-pack>
                    </feature-packs>
                    <layers>
                        <!-- layer provided by the feature pack -->
                        <layer>${artifactId}</layer>
                        <!-- and other layers that are required to run your applications -->
                    </layers>
                </configuration>
            </plugin>
```

# Provision this extension to an existing WildFly installation

Alternatively, you can add this extension to an existing WildFly installation by using [Galleon](https://docs.wildfly.org/galleon/).

After [downloading Galleon](https://github.com/wildfly/galleon/releases/latest), execute the command:

```
galleon.sh install ${groupId}:${artifactId}-feature-pack:${version} --layers=${artifactId} --dir=#[[${]]#JBOSS_HOME}
```

where `#[[${]]#JBOSS_HOME}` is the directory of your WildFly installation.

# References

* [WildFly](https://wildfly.org)
* [WildFly Documentation](https://docs.wildfly.org/)