package ${package};

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.SubsystemResourceRegistration;
import org.jboss.as.controller.capability.RuntimeCapability;
import org.jboss.as.controller.descriptions.ParentResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.SubsystemResourceDescriptionResolver;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.dmr.ModelNode;
import org.wildfly.service.Installer.StartWhen;
import org.wildfly.subsystem.resource.ManagementResourceRegistrar;
import org.wildfly.subsystem.resource.ManagementResourceRegistrationContext;
import org.wildfly.subsystem.resource.ResourceDescriptor;
import org.wildfly.subsystem.resource.capability.CapabilityReference;
import org.wildfly.subsystem.resource.capability.CapabilityReferenceAttributeDefinition;
import org.wildfly.subsystem.resource.operation.ResourceOperationRuntimeHandler;
import org.wildfly.subsystem.service.ResourceServiceConfigurator;
import org.wildfly.subsystem.service.ResourceServiceInstaller;
import org.wildfly.subsystem.service.ServiceDependency;
import org.wildfly.subsystem.service.capability.CapabilityServiceInstaller;

import ${package}.deployment.SubsystemDeploymentProcessor;

/**
 * Registers the resource definition of this subsystem.
 * @author Paul Ferraro
 */
public class SubsystemResourceDefinitionRegistrar implements org.wildfly.subsystem.resource.SubsystemResourceDefinitionRegistrar, Consumer<DeploymentProcessorTarget>, ResourceServiceConfigurator {

    static final SubsystemResourceRegistration REGISTRATION = SubsystemResourceRegistration.of("mysubsystem");

    // This resource provides a capability whose service provides Foo.
    static final RuntimeCapability<Void> CAPABILITY = RuntimeCapability.Builder.of(Foo.SERVICE_DESCRIPTOR).build();

    // An example attribute that references some named capability
    static final CapabilityReferenceAttributeDefinition<Bar> BAR = new CapabilityReferenceAttributeDefinition.Builder<>("bar", CapabilityReference.builder(CAPABILITY, Bar.SERVICE_DESCRIPTOR).build()).build();

    @Override
    public ManagementResourceRegistration register(SubsystemRegistration parent, ManagementResourceRegistrationContext context) {
        ParentResourceDescriptionResolver resolver = new SubsystemResourceDescriptionResolver(REGISTRATION.getName(), SubsystemResourceDefinitionRegistrar.class);

        // Describe attribute and operations of resource
        ResourceDescriptor descriptor = ResourceDescriptor.builder(resolver)
                .addAttributes(List.of(BAR))
                .addCapability(CAPABILITY)
                // Specify runtime behaviour of resource
                .withRuntimeHandler(ResourceOperationRuntimeHandler.configureService(this))
                // If this resource contributes to the deployment chain, register any deployment unit processors
                .withDeploymentChainContributor(this)
                .build();

        // Register the definition of this resource
        ManagementResourceRegistration registration = parent.registerSubsystemModel(ResourceDefinition.builder(REGISTRATION, resolver).build());

        // Registers the attributes, operations, and capabilities of this resource based on our descriptor
        ManagementResourceRegistrar.of(descriptor).register(registration);

        // Register any child resources
        // new MyChildResourceDefinitionRegistrar(resolver).register(registration, context);

        return registration;
    }

    @Override
    public void accept(DeploymentProcessorTarget target) {
        target.addDeploymentProcessor(REGISTRATION.getName(), SubsystemDeploymentProcessor.PHASE, SubsystemDeploymentProcessor.PRIORITY, SubsystemDeploymentProcessor.INSTANCE);
    }

    @Override
    public ResourceServiceInstaller configure(OperationContext context, ModelNode model) throws OperationFailedException {
        // Resolve service dependency from model
        ServiceDependency<Bar> bar = BAR.resolve(context, model);

        Supplier<SimpleFoo> factory = () -> new SimpleFoo(bar.get());
        // e.g. Installs service that create Foo from the specified factory when the service starts
        return CapabilityServiceInstaller.builder(CAPABILITY, factory)
                // Specify any special behaviour on service stop
                .onStop(SimpleFoo::close)
                // Indicate when this service should start
                .startWhen(StartWhen.AVAILABLE) // e.g. Auto-start this service when all of its dependencies have started
                // Specify any dependencies of this service
                .requires(List.of(bar))
                .build();
    }

    /* Sample Foo implementation */
    class SimpleFoo implements Foo, AutoCloseable {
        final Bar bar;

        SimpleFoo(Bar bar) {
            this.bar = bar;
        }

        @Override
        public void close() {
            // Close any resources
        }
    }
}
