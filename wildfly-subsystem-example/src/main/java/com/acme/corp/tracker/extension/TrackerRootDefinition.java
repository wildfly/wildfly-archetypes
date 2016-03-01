package com.acme.corp.tracker.extension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

/**
 * @author <a href="tcerar@redhat.com">Tomaz Cerar</a>
 */
public class TrackerRootDefinition extends PersistentResourceDefinition {

    public static final TrackerRootDefinition INSTANCE = new TrackerRootDefinition();

    private TrackerRootDefinition() {
        super(TrackerExtension.SUBSYSTEM_PATH,
                TrackerExtension.getResourceDescriptionResolver(null),
                //We always need to add an 'add' operation
                TrackerSubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                ReloadRequiredRemoveStepHandler.INSTANCE);
    }

    @Override
    public Collection<AttributeDefinition> getAttributes() {
        return Collections.emptyList();
    }

    @Override
    protected List<? extends PersistentResourceDefinition> getChildren() {
        return Collections.singletonList(TypeDefinition.INSTANCE);
    }

    /**
     * {@inheritDoc}
     * Registers an add operation handler or a remove operation handler if one was provided to the constructor.
     */
    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        //We always need to add a 'describe' operation for root resource
        resourceRegistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);
    }
}
