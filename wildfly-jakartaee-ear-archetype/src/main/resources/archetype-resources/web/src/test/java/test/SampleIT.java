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
        // Create the EAR archive:

        // Use a different name for the ear file that is deployed as part of the test. This is only required if you
        // use the feature to provision a WildFly server during build of the project.
        // This provisioned server already contains the ear file of this project as deployment,
        // so it would not be possible to deploy the same ear containing the tests.
        final String testdeployment = "${rootArtifactId}Tests";

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, testdeployment + "-ear.ear");

        // Current directory is the root of the "${rootArtifactId}-web" project. Go up one level
        // and enter the "ejb" project.
        // The ejb jar is found in the "target" directory:
        File f = new File("../ejb/target/${rootArtifactId}-ejb.jar");
        JavaArchive ejbJar = ShrinkWrap.create(ZipImporter.class, testdeployment + "-ejb.jar").importFrom(f).as(JavaArchive.class);
        ear.addAsModule(ejbJar);

        // Now grab the web archive:
        f = new File("./target/${rootArtifactId}-web.war");
        if (f.exists() == false) {
            throw new RuntimeException("File " + f.getAbsolutePath() + " does not exist.");
        }
        WebArchive war = ShrinkWrap.create(ZipImporter.class, testdeployment + "-web.war").importFrom(f).as(WebArchive.class);
        ear.addAsModule(war);

        // The manifest file is auto created by the Maven EAR plugin - we don't have it here.

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
