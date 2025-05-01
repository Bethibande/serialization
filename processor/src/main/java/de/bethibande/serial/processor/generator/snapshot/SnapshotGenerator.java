package de.bethibande.serial.processor.generator.snapshot;

import com.palantir.javapoet.ClassName;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.BaseTypeGenerator;

public class SnapshotGenerator extends BaseTypeGenerator {

    public SnapshotGenerator() {
        withPostProcessor(new ConstructorGenerator());
    }

    @Override
    protected boolean shouldGenerate(final SerializationContext context) {
        return context.config().reduceBranching();
    }

    @Override
    public ClassName typeName(final SerializationContext context) {
        return context.getSnapshotType();
    }
}
