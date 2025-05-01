package de.bethibande.serial.processor.generator.dto;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.Deserializable;
import de.bethibande.serial.Serializable;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.BaseTypeGenerator;

public class DTOGenerator extends BaseTypeGenerator {

    public DTOGenerator() {
        withPostProcessor(this::addJavaDoc);
        withPostProcessor(this::addInterfaces);
        withPostProcessor(new SizeMethodGenerator());
        withPostProcessor(new WriteMethodGenerator());
        withPostProcessor(new ReadMethodGenerator());
        withPostProcessor(new SnapshotMethodGenerator());
    }

    @Override
    public ClassName typeName(final SerializationContext context) {
        return context.generatedType();
    }

    protected TypeSpec.Builder addJavaDoc(final TypeSpec.Builder builder, final SerializationContext context) {
        return builder.addJavadoc("Generated DTO for class {@link $T}.\n", context.rawType());
    }

    protected TypeSpec.Builder addInterfaces(final TypeSpec.Builder builder, final SerializationContext context) {
        return builder.addSuperinterface(Serializable.class)
                .addSuperinterface(Deserializable.class);
    }

}
