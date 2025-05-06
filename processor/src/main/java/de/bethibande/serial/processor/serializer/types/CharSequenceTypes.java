package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import de.bethibande.serial.processor.serializer.Operations;

@AutoService(FieldBasedObjectTransformer.class)
public class CharSequenceTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && TypeHelper.isCharSequence(field.getType());
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(Operations.WRITE_STRING, FIELD_WRITER, FIELD_VALUE)
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(Operations.READ_STRING, FIELD_READER)
                .build();
    }
}
