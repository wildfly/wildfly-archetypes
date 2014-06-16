package com.acme.corp.tracker.extension;

import org.jboss.as.controller.AbstractWriteAttributeHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class TrackerTickHandler extends AbstractWriteAttributeHandler<Void> {

    public static final TrackerTickHandler INSTANCE = new TrackerTickHandler();

    private TrackerTickHandler() {
        super(TypeDefinition.TICK);
    }


    /**
     * Hook to allow subclasses to make runtime changes to effect the attribute value change.
     *
     * @param context        the context of the operation
     * @param operation      the operation
     * @param attributeName  the name of the attribute being modified
     * @param resolvedValue  the new value for the attribute, after {@link ModelNode#resolve()} has been called on it
     * @param currentValue   the existing value for the attribute
     * @param handbackHolder holder for an arbitrary object to pass to
     *                       {@link #revertUpdateToRuntime(OperationContext, ModelNode, String, ModelNode, ModelNode, Object)} if
     *                       the operation needs to be rolled back
     * @return {@code true} if the server requires restart to effect the attribute
     * value change; {@code false} if not
     */

    protected boolean applyUpdateToRuntime(OperationContext context, ModelNode operation, String attributeName,
                                           ModelNode resolvedValue, ModelNode currentValue, HandbackHolder<Void> handbackHolder) throws OperationFailedException {
        if (attributeName.equals(TrackerExtension.TICK)) {
            final String suffix = PathAddress.pathAddress(operation.get(ModelDescriptionConstants.ADDRESS)).getLastElement().getValue();
            TrackerService service = (TrackerService) context.getServiceRegistry(true).getRequiredService(TrackerService.createServiceName(suffix)).getValue();
            service.setTick(resolvedValue.asLong());
            context.stepCompleted();
        }

        return false;
    }

    /**
     * Hook to allow subclasses to revert runtime changes made in
     * {@link #applyUpdateToRuntime(OperationContext, ModelNode, String, ModelNode, ModelNode, HandbackHolder)}.
     *
     * @param context        the context of the operation
     * @param operation      the operation
     * @param attributeName  the name of the attribute being modified
     * @param valueToRestore the previous value for the attribute, before this operation was executed
     * @param valueToRevert  the new value for the attribute that should be reverted
     * @param handback       an object, if any, passed in to the {@code handbackHolder} by the {@code applyUpdateToRuntime}
     *                       implementation
     */
    protected void revertUpdateToRuntime(OperationContext context, ModelNode operation, String attributeName,
                                         ModelNode valueToRestore, ModelNode valueToRevert, Void handback) {
        // no-op
    }
}
