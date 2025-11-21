package ${package}.deployment;

import ${package}.SubsystemResourceDefinitionRegistrar;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.wildfly.subsystem.service.ServiceDependency;
import org.wildfly.subsystem.service.ServiceInstaller;

/**
 * An example deployment unit processor that does nothing. To add more deployment
 * processors copy this class, and register it with the deployment chain via
 * {@link ${package}.SubsystemResourceDefinitionRegistrar${hash}accept(org.jboss.as.server.DeploymentProcessorTarget)}
 */
public class SubsystemDeploymentProcessor implements DeploymentUnitProcessor {

    /**
     * See {@link Phase} for a description of the different phases
     */
    public static final Phase PHASE = Phase.DEPENDENCIES;
    /**
     * The relative order of this processor within the {@link #PHASE}.
     * The current number is large enough for it to happen after all
     * the standard deployment unit processors that come with JBoss AS.
     */
    public static final int PRIORITY = 0x4000;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        // Install a service that depends on the TrackerService
        ServiceInstaller.builder(ServiceDependency.on(SubsystemResourceDefinitionRegistrar.SERVICE_DESCRIPTOR))
                .onStart(service -> {
                    service.deployments.incrementAndGet();
                })
                .onStop(service -> {
                    service.deployments.decrementAndGet();
                })
                .build()
                .install(phaseContext);
    }
}
