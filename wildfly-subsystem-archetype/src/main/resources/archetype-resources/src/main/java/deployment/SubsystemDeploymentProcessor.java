#set( $hash = '#' )
package ${package}.deployment;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.logging.Logger;

/**
 * An example deployment unit processor that does nothing. To add more deployment
 * processors copy this class, and register it with the deployment chain via
 * {@link ${package}.SubsystemResourceDefinitionRegistrar${hash}accept(org.jboss.as.server.DeploymentProcessorTarget)}
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public enum SubsystemDeploymentProcessor implements DeploymentUnitProcessor {
    INSTANCE;

    private final Logger logger = Logger.getLogger(SubsystemDeploymentProcessor.class);

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
        this.logger.infof("Deploying %s", phaseContext.getDeploymentUnit().getName());
    }

    @Override
    public void undeploy(DeploymentUnit unit) {
        this.logger.infof("Undeploying %s", unit.getName());
    }
}
