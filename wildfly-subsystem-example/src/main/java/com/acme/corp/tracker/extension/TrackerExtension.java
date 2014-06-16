package com.acme.corp.tracker.extension;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Collections;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;


/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TrackerExtension implements Extension {

    /**
     * The name space used for the {@code subsystem} element
     */
    public static final String NAMESPACE = "urn:com.acme.corp.tracker:1.0";

    /**
     * The name of our subsystem within the model.
     */
    public static final String SUBSYSTEM_NAME = "tracker";

    /**
     * The parser used for parsing our subsystem
     */
    private final SubsystemParser parser = new SubsystemParser();

    private static final String RESOURCE_NAME = TrackerExtension.class.getPackage().getName() + ".LocalDescriptions";

    protected static final String TYPE = "type";
    protected static final String TICK = "tick";
    protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);
    protected static final PathElement TYPE_PATH = PathElement.pathElement(TYPE);

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
        return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, TrackerExtension.class.getClassLoader(), true, false);
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, parser);
    }

    @Override
    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, 1, 0);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(TrackerSubsystemDefinition.INSTANCE);


        //Add the type child
        ManagementResourceRegistration typeChild = registration.registerSubModel(TypeDefinition.INSTANCE);
        subsystem.registerXMLElementWriter(parser);
    }


    /**
     * The subsystem parser, which uses stax to read and write to and from xml
     */
    private static class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            // Require no attributes
            ParseUtils.requireNoAttributes(reader);

            //Add the main subsystem 'add' operation
            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).set(PathAddress.pathAddress(SUBSYSTEM_PATH).toModelNode());
            list.add(subsystem);

            //Read the children
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                if (!reader.getLocalName().equals("deployment-types")) {
                    throw ParseUtils.unexpectedElement(reader);
                }
                while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                    if (reader.isStartElement()) {
                        readDeploymentType(reader, list);
                    }
                }
            }
        }

        private void readDeploymentType(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            if (!reader.getLocalName().equals("deployment-type")) {
                throw ParseUtils.unexpectedElement(reader);
            }
            ModelNode addTypeOperation = new ModelNode();
            addTypeOperation.get(OP).set(ModelDescriptionConstants.ADD);

            String suffix = null;
            for (int i = 0; i < reader.getAttributeCount(); i++) {
                String attr = reader.getAttributeLocalName(i);
                String value = reader.getAttributeValue(i);
                if (attr.equals("tick")) {
                    TypeDefinition.TICK.parseAndSetParameter(value, addTypeOperation, reader);
                } else if (attr.equals("suffix")) {
                    suffix = value;
                } else {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }
            ParseUtils.requireNoContent(reader);
            if (suffix == null) {
                throw ParseUtils.missingRequiredElement(reader, Collections.singleton("suffix"));
            }

            //Add the 'add' operation for each 'type' child
            PathAddress addr = PathAddress.pathAddress(SUBSYSTEM_PATH, PathElement.pathElement(TYPE, suffix));
            addTypeOperation.get(OP_ADDR).set(addr.toModelNode());
            list.add(addTypeOperation);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeContent(final XMLExtendedStreamWriter writer, final SubsystemMarshallingContext context) throws XMLStreamException {
            //Write out the main subsystem element
            context.startSubsystemElement(TrackerExtension.NAMESPACE, false);
            writer.writeStartElement("deployment-types");
            ModelNode node = context.getModelNode();
            ModelNode type = node.get(TYPE);
            for (Property property : type.asPropertyList()) {

                //write each child element to xml
                writer.writeStartElement("deployment-type");
                writer.writeAttribute("suffix", property.getName());
                ModelNode entry = property.getValue();
                TypeDefinition.TICK.marshallAsAttribute(entry, true, writer);
                writer.writeEndElement();
            }
            //End deployment-types
            writer.writeEndElement();
            //End subsystem
            writer.writeEndElement();
        }
    }


}
