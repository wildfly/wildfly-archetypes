package ${package};

import static ${package}.SubsystemResourceDefinitionRegistrar.EXECUTOR;
import static ${package}.SubsystemResourceDefinitionRegistrar.TICK;

import java.util.List;

import org.jboss.as.controller.persistence.xml.ResourceXMLParticleFactory;
import org.jboss.as.controller.persistence.xml.SubsystemResourceRegistrationXMLElement;
import org.jboss.as.controller.persistence.xml.SubsystemResourceXMLSchema;
import org.jboss.as.controller.xml.IntVersionSchema;
import org.jboss.as.controller.xml.VersionedNamespace;
import org.jboss.staxmapper.IntVersion;

/**
 * Enumerates the XML schema namespaces for this subsystem.
 */
public enum SubsystemSchema implements SubsystemResourceXMLSchema<SubsystemSchema> {
    VERSION_1_0(1, 0);

    static final SubsystemSchema CURRENT = VERSION_1_0;

    private final VersionedNamespace<IntVersion, SubsystemSchema> namespace;
    private final ResourceXMLParticleFactory factory = ResourceXMLParticleFactory.newInstance(this);

    SubsystemSchema(int major, int minor) {
        this.namespace = IntVersionSchema.createURN(List.of("${groupId}", "${artifactId}"), new IntVersion(major, minor));
    }

    @Override
    public VersionedNamespace<IntVersion, SubsystemSchema> getNamespace() {
        return this.namespace;
    }

    @Override
    public SubsystemResourceRegistrationXMLElement getSubsystemXMLElement() {
        return this.factory.subsystemElement(SubsystemResourceDefinitionRegistrar.REGISTRATION)
                .addAttributes(List.of(TICK, EXECUTOR))
                .build();
    }
}