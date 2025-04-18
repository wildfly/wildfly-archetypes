package ${package}.test;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Sample integration test: demonstrates how to create the WAR file using the ShrinkWrap API.
 *
 * Delete this file if no integration test is required.
 */
@ExtendWith(ArquillianExtension.class)
public class SampleIT {

    /**
     * Creates the WAR file that is deployed to the server.
     *
     * @return WAR archive
     */
    @Deployment
    public static Archive<?> getEarArchive() {
        // Use a different name for the war file that is deployed as part of the test. This is only required if you
        // use the feature to provision a WildFly server during build of the project.
        // This provisioned server already contains the war file of this project as deployment,
        // so it would not be possible to deploy the same war containing the tests.
        final String testdeployment = "${rootArtifactId}Tests.war";

        // Import the web archive that was created by Maven:
        File f = new File("./target/${rootArtifactId}.war");
        if (f.exists() == false) {
            throw new RuntimeException("File " + f.getAbsolutePath() + " does not exist.");
        }
        WebArchive war = ShrinkWrap.create(ZipImporter.class, testdeployment).importFrom(f).as(WebArchive.class);

        // Add the package containing the test classes:
        war.addPackage("${package}.test");

        // Export the WAR file to examine it in case of problems:
        // war.as(ZipExporter.class).exportTo(new File("c:\\temp\\test.war"), true);

        return war;
    }

    /**
     * A sample test...
     *
     */
    @Test
    public void test() {
        // This line will be written on the server console.
        System.out.println("Test is invoked...");
    }
}
