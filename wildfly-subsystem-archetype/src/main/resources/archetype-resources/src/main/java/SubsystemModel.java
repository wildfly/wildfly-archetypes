package ${package};

import org.jboss.as.controller.ModelVersion;

/**
 * Enumerates the model versions of this subsystem.
 * @author Paul Ferraro
 */
public enum SubsystemModel implements org.jboss.as.controller.SubsystemModel {
    VERSION_1_0_0(1, 0, 0),
    ;
    static final SubsystemModel CURRENT = VERSION_1_0_0;

    private final ModelVersion version;

    SubsystemModel(int major, int minor, int micro) {
        this.version = ModelVersion.create(major);
    }

    @Override
    public ModelVersion getVersion() {
        return this.version;
    }
}
