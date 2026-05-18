package ${package};

import java.io.IOException;
import java.util.EnumSet;

import org.jboss.as.controller.capability.RuntimeCapability;
import org.jboss.as.subsystem.test.AbstractSubsystemSchemaTest;
import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This is the bare bones test example that tests subsystem
 * It does same things that {@link SubsystemParsingTestCase} does but most of internals are already done in AbstractSubsystemBaseTest
 * If you need more control over what happens in tests look at  {@link SubsystemParsingTestCase}
 */
@RunWith(Parameterized.class)
public class SubsystemBaseParsingTestCase extends AbstractSubsystemSchemaTest<SubsystemSchema> {

    private final SubsystemSchema schema;

    public SubsystemBaseParsingTestCase(SubsystemSchema schema) {
        super(SubsystemResourceDefinitionRegistrar.REGISTRATION.getName(), new Extension(), schema, SubsystemSchema.CURRENT);
        this.schema = schema;
    }

    @Parameters
    public static Iterable<SubsystemSchema> parameters() {
        return EnumSet.allOf(SubsystemSchema.class);
    }

    @Override
    protected String getSubsystemXsdPathPattern() {
        return "schema/%1$s_%2$d_%3$d.xsd";
    }

    @Override
    protected AdditionalInitialization createAdditionalInitialization() {
        return AdditionalInitialization.withCapabilities(
                RuntimeCapability.resolveCapabilityName(SubsystemResourceDefinitionRegistrar.EXECUTOR_SERVICE, "default"));
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return switch (this.schema) {
            case VERSION_1_0 -> """
                    <subsystem xmlns="urn:${groupId}:${artifactId}:1.0"
                        tick="4"
                        executor="default" />""";

            default -> throw new IllegalArgumentException(this.schema.getNamespace().getUri());
        };
    }
}