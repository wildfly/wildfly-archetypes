package ${package};

import org.jboss.as.controller.capability.RuntimeCapability;
import org.jboss.as.subsystem.test.AbstractSubsystemSchemaTest;
import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.EnumSet;

/**
 * This is the bare bones test example that tests subsystem
 * It does same things that {@link SubsystemParsingTestCase} does but most of internals are already done in AbstractSubsystemBaseTest
 * If you need more control over what happens in tests look at  {@link SubsystemParsingTestCase}
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
@RunWith(Parameterized.class)
public class SubsystemBaseParsingTestCase extends AbstractSubsystemSchemaTest<SubsystemSchema> {

    @Parameters
    public static Iterable<SubsystemSchema> parameters() {
        return EnumSet.allOf(SubsystemSchema.class);
    }

    private final SubsystemSchema schema;

    public SubsystemBaseParsingTestCase(SubsystemSchema schema) {
        super(SubsystemResourceDefinitionRegistrar.REGISTRATION.getName(), new SubsystemExtension(), schema, SubsystemSchema.CURRENT);
        this.schema = schema;
    }

    @Override
    protected String getSubsystemXsdPathPattern() {
        return "schema/%1$s_%2$d_%3$d.xsd";
    }

    @Override
    protected AdditionalInitialization createAdditionalInitialization() {
        return AdditionalInitialization.withCapabilities(RuntimeCapability.resolveCapabilityName(Bar.SERVICE_DESCRIPTOR, "test"));
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return switch (this.schema) {
            case VERSION_1_0 -> """
<subsystem xmlns="urn:mycompany:mysubsystem:1.0" bar="test"/>""";
            default -> throw new IllegalArgumentException(this.schema.getNamespace().getUri());
        };
    }
}
