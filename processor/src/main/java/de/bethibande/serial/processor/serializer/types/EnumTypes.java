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
                && field.getTypeElement() != null // Primitive types such as int don't have a type element
                && field.getTypeElement().getKind() == ElementKind.ENUM;
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("$L.writeInt($L.ordinal())", "target", field.getFieldName())
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("return $T.values()[reader.readInt()]", field.getTypeElement())
                .build();
    }
}
