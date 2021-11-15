This project was created from the archetype "wildfly-subsystem-archetype".

Documentation on Subsystems can be found here: https://docs.wildfly.org/25/Extending_WildFly.html

To deploy it:

Step 1:
Register the extension in "standalone.xml" and add the subsystem configuration:

    <extensions>
        ...
        <extension module="$module"/>
    </extensions>

    <profile>
        ...
        <subsystem xmlns="urn:mycompany:mysubsystem:1.0">
        </subsystem>
		
    </profile>

Step 2:
After having built this project with a call to "mvn install", the directory "target/module" will contain a subdirectoy structure
corresponding to the module name "$module" - each "." is replaced with a path separator.
This subdirectory contains two files "${artifactId}.jar" and "module.xml".
Copy the subdirectory of "target/module" to "%WILDFLY_HOME%/modules/system/layers/base/.../main".


Now start the WildFly server.