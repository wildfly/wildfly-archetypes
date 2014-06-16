package com.acme.corp.tracker.extension;

import static com.acme.corp.tracker.extension.TrackerExtension.TYPE;
import static com.acme.corp.tracker.extension.TrackerExtension.TYPE_PATH;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="tcerar@redhat.com">Tomaz Cerar</a>
 */
public class TypeDefinition extends SimpleResourceDefinition {

    protected static final SimpleAttributeDefinition TICK =
            new SimpleAttributeDefinitionBuilder(TrackerExtension.TICK, ModelType.LONG)
                    .setAllowExpression(true)
                    .setDefaultValue(new ModelNode(1000))
                    .setAllowNull(false)
                    .build();

    public static final TypeDefinition INSTANCE = new TypeDefinition();

    private TypeDefinition() {
        super(TYPE_PATH,
                TrackerExtension.getResourceDescriptionResolver(TYPE),
                //We always need to add an 'add' operation
                TypeAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                TypeRemove.INSTANCE);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerReadWriteAttribute(TICK, null, TrackerTickHandler.INSTANCE);
    }
}
