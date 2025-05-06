package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import de.bethibande.serial.processor.serializer.Operations;

import javax.lang.model.type.TypeKind;

@AutoService(FieldBasedObjectTransformer.class)
public class PrimitiveTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return field.getType().getKind().isPrimitive();
    }

    protected TypeKind toTypeKind(final FieldInfo field) {
        return field.getType().getKind();
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(Operations.writeOperation(toTypeKind(field)), FIELD_WRITER, FIELD_VALUE)
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(Operations.readOperation(toTypeKind(field)), FIELD_READER)
                .build();
    }
}
