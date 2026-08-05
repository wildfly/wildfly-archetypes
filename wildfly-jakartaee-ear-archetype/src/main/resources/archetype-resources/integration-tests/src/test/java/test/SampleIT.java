package ${package}.test;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Sample integration test: demonstrates how to create the EAR file using the ShrinkWrap API.
 *
 * Delete this file if no integration test is required.
 */
@ExtendWith(ArquillianExtension.class)
public class SampleIT {

    /**
     * Creates the EAR file that is deployed to the server.
     *
     * @return EAR archive
     */
    @Deployment
    public static Archive<?> getEarArchive() {

        // Open the existing EAR archive:
        File f = new File("../ear/target/${rootArtifactId}.ear");
        EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "${rootArtifactId}.ear").importFrom(f).as(EnterpriseArchive.class);

        // Now grab the web archive:
        WebArchive war = ear.getAsType(WebArchive.class, "${rootArtifactId}-web.war");

        // Add the package containing the test classes:
        war.addPackage("${package}.test");

        // Export the EAR file to examine it in case of problems:
        // ear.as(ZipExporter.class).exportTo(new File("c:\\temp\\test.ear"), true);

        return ear;
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
