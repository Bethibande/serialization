package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.attributes.AttributeMap;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.EmbeddedTypeTransformer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import java.util.Collection;

@AutoService(FieldBasedObjectTransformer.class)
public class NullableFieldTypes extends EmbeddedTypeTransformer {

    private static final String NOT_NULL_METHOD_SUFFIX = "NotNull";

    @Override
    protected boolean isApplicable0(final FieldInfo field, final ElementSerializer serializer) {
        return field.isNullable();
    }

    @Override
    protected FieldInfo createComponentInfo(final FieldInfo field) {
        return field.toBuilder()
                .attributes(new AttributeMap())
                .nullable(false)
                .build();
    }

    @Override
    protected String childMethodSuffix() {
        return NOT_NULL_METHOD_SUFFIX;
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if ($L != null)", field.getFieldName())
                .addStatement("target.writeBoolean(true)")
                .addStatement("return $L($L)", field.getFieldName() + NOT_NULL_METHOD_SUFFIX, field.getFieldName())
                .endControlFlow()
                .addStatement("target.writeBoolean(false)")
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if (reader.readBoolean())")
                .addStatement("return $L()", field.getFieldName() + NOT_NULL_METHOD_SUFFIX)
                .endControlFlow()
                .addStatement("return null")
                .build();
    }
}
