package com.acme.corp.tracker.extension;


import java.io.IOException;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;


/**
 * @author Tomaz Cerar
 */
public class SubsystemBaseParsingTestCase extends AbstractSubsystemBaseTest {

    public SubsystemBaseParsingTestCase() {
        super(TrackerExtension.SUBSYSTEM_NAME, new TrackerExtension());
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("subsystem.xml");
    }
}
