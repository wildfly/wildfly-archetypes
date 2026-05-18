package ${package};

import org.wildfly.subsystem.SubsystemConfiguration;
import org.wildfly.subsystem.SubsystemPersistence;

/**
 * This class defines the extension.
 */
public class Extension extends org.wildfly.subsystem.SubsystemExtension<SubsystemSchema> {

    public Extension() {
        super(SubsystemConfiguration.of(SubsystemResourceDefinitionRegistrar.REGISTRATION,
                        SubsystemModel.CURRENT,
                        SubsystemResourceDefinitionRegistrar::new),
                SubsystemPersistence.of(SubsystemSchema.CURRENT));
    }
}
