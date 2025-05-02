package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.ElementKind;

@AutoService(FieldBasedObjectTransformer.class)
public class EnumTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable()
                && field.getTypeElement() != null
                && field.getTypeElement().getKind() == ElementKind.ENUM;
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("$L.writeInt($L.ordinal())", "writer", "value")
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("return $T.values()[$L.readInt()]", field.getType(), "reader")
                .build();
    }
}
