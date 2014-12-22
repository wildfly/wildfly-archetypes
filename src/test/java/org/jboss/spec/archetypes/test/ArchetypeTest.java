/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.spec.archetypes.test;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Test;

/**
 * @author Rafael Benevides
 * 
 */
public class ArchetypeTest {

    private static Log log = LogFactory.getLog(ArchetypeTest.class);

    private String outputDir = System.getProperty("outPutDirectory");

    private String testOutputDirectory = System.getProperty("testOutputDirectory");

    private String baseDir = System.getProperty("basedir");

    private boolean onlyMavenCentral = Boolean.parseBoolean(System.getProperty("onlyMavenCentral", "true"));

    private boolean cleanArchetypes = Boolean.parseBoolean(System.getProperty("cleanArchetypes"));

    @Test
    public void testArchetypes() throws Exception {
        log.info(String.format("Running tests... onlyMavenCentral: %s - cleanArchetypes: %s", onlyMavenCentral, cleanArchetypes));
        File baseDirFile = new File(baseDir);
        for (File file : baseDirFile.listFiles()) {
            if (file.isDirectory() && isMavenProject(file) && file.getName().startsWith("wildfly-javaee7")) {
                Reader reader = new FileReader(new File(file, "pom.xml"));
                try {
                    MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
                    Model model = xpp3Reader.read(reader);

                    installArchetype(file, model);

                    executeCreateArchetype(model);
                } finally {
                    reader.close();
                }
            }
        }
    }

    /**
     * @param baseDir
     * @param model
     * @throws VerificationException
     */
    private void installArchetype(File baseDir, Model model) throws VerificationException {
        log.info("Installing Archetype " + model);
        Verifier installer = new Verifier(baseDir.getAbsolutePath());
        if (onlyMavenCentral) {
            installer.addCliOption("-s " + testOutputDirectory + File.separator + "settings-clear.xml");
        }
        installer.setLogFileName("install.log");
        installer.setAutoclean(cleanArchetypes);
        installer.executeGoal("install");

        // Remove install.log from inside archetype
        new File(baseDir, "install.log").delete();
    }

    /**
     * @param file
     * @return
     */
    private boolean isMavenProject(File dir) {
        return new File(dir, "pom.xml").exists();
    }

    /**
     * @param archetypeVersion
     * @throws ComponentLookupException
     * @throws PlexusContainerException
     */
    private void executeCreateArchetype(Model model) throws Exception {
        log.info("Creating project from Archetype: " + model);
        String goal = "org.apache.maven.plugins:maven-archetype-plugin:2.2:generate";
        Properties properties = new Properties();
        properties.put("archetypeGroupId", model.getGroupId());
        properties.put("archetypeArtifactId", model.getArtifactId());
        properties.put("archetypeVersion", model.getVersion());
        properties.put("groupId", "org.jboss.as.quickstarts");
        String artifactId = System.currentTimeMillis() + "-" + model.toString().replaceAll("[^a-zA-Z_0-9]", "");
        properties.put("artifactId", artifactId);
        properties.put("version", "0.0.1-SNAPSHOT");
        Verifier verifier = new org.apache.maven.it.Verifier(outputDir);
        verifier.setAutoclean(false);
        verifier.setSystemProperties(properties);
        verifier.setLogFileName(artifactId + "-generate.txt");

        verifier.executeGoal(goal);

        log.info("Building project from Archetype: " + model);
        Verifier buildVerifier = new Verifier(outputDir + File.separator + artifactId);
        if (onlyMavenCentral) {
            buildVerifier.addCliOption("-s " + testOutputDirectory + File.separator + "settings-clear.xml");
        }
        buildVerifier.executeGoal("compile"); // buildVerifier log is inside each project
        String functionalTestsFolder = outputDir + File.separator + artifactId + File.separator + "functional-tests";
        if (new File(functionalTestsFolder).exists()) {
            log.info("Building functional-tests from: " + functionalTestsFolder);
            Verifier functionalTestsVerifier = new Verifier(functionalTestsFolder);
            functionalTestsVerifier.setAutoclean(cleanArchetypes);
            if (onlyMavenCentral) {
                functionalTestsVerifier.addCliOption("-s " + testOutputDirectory + File.separator + "settings-clear.xml");
            }
            functionalTestsVerifier.executeGoal("compile");
        }
    }
}
