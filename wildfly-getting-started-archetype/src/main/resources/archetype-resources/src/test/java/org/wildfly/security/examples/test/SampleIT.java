#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.wildfly.security.examples.test;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Sample integration test: demonstrates how to create the WAR file using the ShrinkWrap API.
 * 
 * Delete this file if no integration test is required.
 */
@RunWith(Arquillian.class)
public class SampleIT {

    /**
     * Creates the WAR file that is deployed to the server.
     * 
     * @return WAR archive
     */
    @Deployment
    public static Archive<?> getEarArchive() {
        // Import the web archive that was created by Maven:
        File f = new File("./target/login-form.war");
        if (f.exists() == false) {
            throw new RuntimeException("File " + f.getAbsolutePath() + " does not exist.");
        }
        WebArchive war = ShrinkWrap.create(ZipImporter.class, "login-form.war").importFrom(f).as(WebArchive.class);
        
        // Add the package containing the test classes:
        war.addPackage("org.wildfly.security.examples.test");

        // Export the WAR file to examine it in case of problems:
        // war.as(ZipExporter.class).exportTo(new File("c:${symbol_escape}${symbol_escape}temp${symbol_escape}${symbol_escape}test.war"), true);

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
