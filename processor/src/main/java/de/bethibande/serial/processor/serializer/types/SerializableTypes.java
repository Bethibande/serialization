package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import de.bethibande.serial.annotations.SerializableType;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.Modifier;

@AutoService(FieldBasedObjectTransformer.class)
public class SerializableTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && field.getTypeElement().getAnnotation(SerializableType.class) != null;
    }

    protected String serializerFieldName(final FieldInfo field) {
        return methodName(field) + "Serializer";
    }

    protected TypeName serializerType(final FieldInfo field) {
        return TypeHelper.adaptTypeName(ClassName.get(field.getTypeElement()), "Serializer");
    }

    @Override
    public void applySerializerTransformation(final TypeSpec.Builder builder, final FieldInfo field, final SerializationContext ctx) {
        final TypeName serializerType = serializerType(field);

        final FieldSpec serializerField = FieldSpec.builder(serializerType, serializerFieldName(field))
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", serializerType)
                .build();

        final MethodSpec serializerMethod = MethodSpec.methodBuilder(serializerFieldName(field))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(serializerType(field), serializerFieldName(field))
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addStatement("this.$L = $L", serializerFieldName(field), serializerFieldName(field))
                .addStatement("return this")
                .returns(ctx.serializerType())
                .build();

        builder.addField(serializerField);
        builder.addMethod(serializerMethod);
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("$L.bind($L).write($L)", serializerFieldName(field), FIELD_WRITER, FIELD_VALUE)
                .addStatement("return this")
                .build();
    }

    protected String deserializerFieldName(final FieldInfo field) {
        return methodName(field) + "Deserializer";
    }

    protected TypeName deserializerType(final FieldInfo field) {
        return TypeHelper.adaptTypeName(ClassName.get(field.getTypeElement()), "Deserializer");
    }

    @Override
    public void applyDeserializerTransformation(final TypeSpec.Builder builder, final FieldInfo field, final SerializationContext ctx) {
        final TypeName deserializerType = deserializerType(field);

        final FieldSpec deserializerField = FieldSpec.builder(deserializerType, deserializerFieldName(field))
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", deserializerType)
                .build();

        final MethodSpec deserializerMethod = MethodSpec.methodBuilder(deserializerFieldName(field))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(deserializerType(field), deserializerFieldName(field))
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addStatement("this.$L = $L", deserializerFieldName(field), deserializerFieldName(field))
                .addStatement("return this")
                .returns(ctx.deserializerType())
                .build();

        builder.addField(deserializerField);
        builder.addMethod(deserializerMethod);
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("return $L.bind($L).read()", deserializerFieldName(field), FIELD_READER)
                .build();
    }
}
