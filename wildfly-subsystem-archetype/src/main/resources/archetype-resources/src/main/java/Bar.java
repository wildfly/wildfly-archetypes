package ${package};

import org.wildfly.service.descriptor.UnaryServiceDescriptor;

/**
 * Example dependency of the capability provided by our subsystem.
 * This would typically reside in some SPI module on which this module can depend.
 * @author Paul Ferraro
 */
public interface Bar {
    UnaryServiceDescriptor<Bar> SERVICE_DESCRIPTOR = UnaryServiceDescriptor.of("mycompany.bar", Bar.class);
}
