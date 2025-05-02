package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.attributes.AttributeMap;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.EmbeddedTypeTransformer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@AutoService(FieldBasedObjectTransformer.class)
public class ArrayTypes extends EmbeddedTypeTransformer {

    private static final String ARRAY_VALUE_METHOD_SUFFIX = "ArrayValue";

    @Override
    protected boolean isApplicable0(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && field.getType().getKind() == TypeKind.ARRAY;
    }

    @Override
    protected FieldInfo createComponentInfo(final FieldInfo field) {
        final TypeMirror componentType = unwrapArrayType(field.getType());
        return field.toBuilder()
                .attributes(new AttributeMap())
                .nullable(TypeHelper.isNullable(componentType))
                .type(componentType)
                .build();
    }

    @Override
    protected String childMethodSuffix() {
        return ARRAY_VALUE_METHOD_SUFFIX;
    }

    private TypeMirror unwrapArrayType(final TypeMirror type) {
        if (!(type instanceof ArrayType arrayType)) throw new IllegalArgumentException("Type is not an array: " + type);
        return arrayType.getComponentType();
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("target.writeInt($L.length)", field.getFieldName())
                .beginControlFlow("for (int i = 0; i < $L.length; i++)", field.getFieldName())
                .addStatement("$L($L[i])", field.getFieldName() + ARRAY_VALUE_METHOD_SUFFIX, field.getFieldName())
                .endControlFlow()
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        final FieldInfo componentInfo = getComponentInfo(field);

        return CodeBlock.builder()
                .addStatement("final int length = reader.readInt()")
                .addStatement("final $T $L = new $T[length]", field.getTypeName(), field.getFieldName(), componentInfo.getTypeName())
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("$L[i] = $L()", field.getFieldName(), field.getFieldName() + ARRAY_VALUE_METHOD_SUFFIX)
                .endControlFlow()
                .addStatement("return $L", field.getFieldName())
                .build();
    }
}
