package ${package}.testsuite;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Arquillian test that verifies the ${artifactId} subsystem's deployments attribute.
 * <p>
 * This test:
 * 1. Verifies deployments count is 0 before deployment
 * 2. Deploys an empty WAR file
 * 3. Verifies deployments count is 1 after deployment
 * 4. Undeploys the WAR file
 * 5. Verifies deployments count is 0 after undeployment
 */
@RunWith(Arquillian.class)
@RunAsClient
public class DeploymentTestCase {

    private static final String DEPLOYMENT_NAME = "${artifactId}-test";

    @ArquillianResource
    private ManagementClient managementClient;

    @ArquillianResource
    private Deployer deployer;

    @Deployment(name = DEPLOYMENT_NAME, managed = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, DEPLOYMENT_NAME + ".war");
    }

    @Test
    public void testDeploymentTracking() throws Exception {
        ModelControllerClient client = managementClient.getControllerClient();

        // Verify deployments count is 0 before deployment
        int deploymentsBeforeDeployment = readDeploymentsAttribute(client);
        Assert.assertEquals("Expected 0 deployments before deployment", 0, deploymentsBeforeDeployment);

        // Deploy the WAR
        deployer.deploy(DEPLOYMENT_NAME);

        // Verify deployments count is 1 after deployment
        int deploymentsAfterDeployment = readDeploymentsAttribute(client);
        Assert.assertEquals("Expected 1 deployment after deployment", 1, deploymentsAfterDeployment);

        // Undeploy the WAR
        deployer.undeploy(DEPLOYMENT_NAME);

        // Verify deployments count is 0 after undeployment
        int deploymentsAfterUndeployment = readDeploymentsAttribute(client);
        Assert.assertEquals("Expected 0 deployments after undeployment", 0, deploymentsAfterUndeployment);
    }

    /**
     * Reads the deployments attribute from the tracker subsystem.
     * Executes: /subsystem=${artifactId}:read-attribute(name=deployments)
     */
    private int readDeploymentsAttribute(ModelControllerClient client) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(ClientConstants.OP).set(ClientConstants.READ_ATTRIBUTE_OPERATION);
        operation.get(ClientConstants.OP_ADDR).add("subsystem", "${artifactId}");
        operation.get(ClientConstants.NAME).set("deployments");

        ModelNode result = client.execute(operation);

        if (!result.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
            throw new RuntimeException("Operation failed: " + result.get(ClientConstants.FAILURE_DESCRIPTION));
        }

        return result.get(ClientConstants.RESULT).asInt();
    }
}
