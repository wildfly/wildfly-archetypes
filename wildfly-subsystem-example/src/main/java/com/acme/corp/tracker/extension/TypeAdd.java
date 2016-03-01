package com.acme.corp.tracker.extension;

import static com.acme.corp.tracker.extension.TypeDefinition.TICK;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;

/**
 * @author Tomaz Cerar
 */
class TypeAdd extends AbstractAddStepHandler {

    public static final TypeAdd INSTANCE = new TypeAdd();

    private TypeAdd() {
        super(TICK);//call super with parameters that this handler takes
    }


    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model)
            throws OperationFailedException {
        String suffix = context.getCurrentAddressValue();
        // we use resolveModelAttribute to properly resolve any expressions
        // and to use the default value if otherwise undefined
        long tick = TICK.resolveModelAttribute(context, model).asLong();
        TrackerService service = new TrackerService(suffix, tick);
        ServiceName name = TrackerService.createServiceName(suffix);
        context.getServiceTarget()
                .addService(name, service)
                .setInitialMode(Mode.ACTIVE)
                .install();
    }
}
