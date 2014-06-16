package com.acme.corp.tracker.extension;

import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;

/**
 * @author <a href="tcerar@redhat.com">Tomaz Cerar</a>
 */
public class TrackerSubsystemDefinition extends SimpleResourceDefinition {

    public static final TrackerSubsystemDefinition INSTANCE = new TrackerSubsystemDefinition();

    private TrackerSubsystemDefinition() {
        super(TrackerExtension.SUBSYSTEM_PATH,
                TrackerExtension.getResourceDescriptionResolver(null),
                //We always need to add an 'add' operation
                SubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                ReloadRequiredRemoveStepHandler.INSTANCE);
    }

    /**
     * {@inheritDoc}
     * Registers an add operation handler or a remove operation handler if one was provided to the constructor.
     */
    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        //We always need to add a 'describe' operation
        resourceRegistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);
    }
}
