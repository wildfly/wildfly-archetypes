package com.acme.corp.tracker.extension;

import static org.jboss.as.controller.PersistentResourceXMLDescription.builder;

import org.jboss.as.controller.PersistentResourceXMLDescription;
import org.jboss.as.controller.PersistentResourceXMLParser;

/**
 * The subsystem parser, which uses stax to read and write to and from xml
 */
class TrackerParser1_0 extends PersistentResourceXMLParser {

    private static final PersistentResourceXMLDescription xmlDescription =
            builder(TrackerRootDefinition.INSTANCE, TrackerExtension.NAMESPACE)
                    .addChild(
                            builder(TypeDefinition.INSTANCE)
                                    .setXmlElementName("deployment-type")
                                    .addAttributes(TypeDefinition.TICK)
                    )
                    .build();

    @Override
    public PersistentResourceXMLDescription getParserDescription() {
        return xmlDescription;
    }

}
