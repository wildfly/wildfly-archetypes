package ${package};

import org.wildfly.service.descriptor.NullaryServiceDescriptor;

/**
 * Example capability provided by this subsystem.
 * Assuming this is consumed by another subsystem, this interface would typically reside in a separate SPI module.
 * @author Paul Ferraro
 */
public interface Foo {
    NullaryServiceDescriptor<Foo> SERVICE_DESCRIPTOR = NullaryServiceDescriptor.of("mycompany.foo", Foo.class);
}
