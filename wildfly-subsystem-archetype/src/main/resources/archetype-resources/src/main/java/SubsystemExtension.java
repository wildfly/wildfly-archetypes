package ${package};

import org.wildfly.subsystem.SubsystemConfiguration;
import org.wildfly.subsystem.SubsystemPersistence;

/**
 * @author Paul Ferraro
 */
public class SubsystemExtension extends org.wildfly.subsystem.SubsystemExtension<SubsystemSchema> {

    public SubsystemExtension() {
        super(SubsystemConfiguration.of(SubsystemResourceDefinitionRegistrar.REGISTRATION, SubsystemModel.CURRENT, SubsystemResourceDefinitionRegistrar::new), SubsystemPersistence.of(SubsystemSchema.CURRENT));
    }
}
