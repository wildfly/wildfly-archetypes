package ${package};

import ${package}.deployment.SubsystemDeploymentProcessor;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.dmr.ModelNode;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final SubsystemAdd INSTANCE = new SubsystemAdd();

    private SubsystemAdd() {
        super(SubsystemDefinition.ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, Resource resource)
            throws OperationFailedException {

        installServices(context, operation, resource.getModel());

        //Add deployment processors here
        //Remove this if you don't need to hook into the deployers, or you can add as many as you like
        //see SubDeploymentProcessor for explanation of the phases
        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME, SubsystemDeploymentProcessor.PHASE, SubsystemDeploymentProcessor.PRIORITY, new SubsystemDeploymentProcessor());

            }
        }, OperationContext.Stage.RUNTIME);

    }

    static void installServices(OperationContext context, ModelNode operation, ModelNode model) {
        // Add any services here
        //context.getServiceTarget().addService(ServiceName.of("some", "name"), new MyService()).install();
    }
}
