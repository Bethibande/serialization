package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.attributes.AttributeKey;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import java.util.Collection;
import java.util.List;

@AutoService(FieldBasedObjectTransformer.class)
public class NullableFieldTypes implements FieldBasedObjectTransformer {

    public static final AttributeKey<FieldBasedObjectTransformer> CHILD_TRANSFORMER = AttributeKey.of("nullable.childTransformer");

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        if (!field.isNullable()) return false;

        field.setNullable(false);
        final FieldBasedObjectTransformer transformer = serializer.getFieldTransformer(field).orElseThrow();
        field.setNullable(true);

        field.set(CHILD_TRANSFORMER, transformer);

        return true;
    }

    private MethodSpec embedTransformer(final FieldInfo field,
                                        final MethodType methodType,
                                        final FieldBasedObjectTransformer transformer,
                                        final SerializationContext ctx) {
        switch (methodType) {
            case READ -> transformer.transformDeserializer(TypeSpec.classBuilder("Temp"), List.of(field), ctx);
            case WRITE -> transformer.transformSerializer(TypeSpec.classBuilder("Temp"), List.of(field), ctx);
        }

        return field.getGeneratedMethod(methodType);
    }

    @Override
    public void transformSerializer(final TypeSpec.Builder builder,
                                    final Collection<FieldInfo> fields,
                                    final SerializationContext ctx) {
        for (final FieldInfo field : fields) {
            final FieldBasedObjectTransformer childTransformer = field.get(CHILD_TRANSFORMER).orElseThrow();
            final MethodSpec childSpec = embedTransformer(field, MethodType.WRITE, childTransformer, ctx)
                    .toBuilder()
                    .setName(field.getFieldName() + "NotNull")
                    .returns(ctx.serializerType())
                    .build();

            final MethodSpec method = defaultWriteMethod(field, ctx)
                    .beginControlFlow("if ($L != null)", field.getFieldName())
                    .addStatement("target.writeBoolean(true)")
                    .addStatement("return $L($L)", childSpec.name(), field.getFieldName())
                    .endControlFlow()
                    .addStatement("target.writeBoolean(false)")
                    .addStatement("return this")
                    .build();

            field.addGeneratedMethod(MethodType.WRITE, method);
            builder.addMethod(method);
            builder.addMethod(childSpec);
        }
    }

    @Override
    public void transformDeserializer(final TypeSpec.Builder builder,
                                      final Collection<FieldInfo> fields,
                                      final SerializationContext ctx) {
        for (final FieldInfo field : fields) {
            final FieldBasedObjectTransformer childTransformer = field.get(CHILD_TRANSFORMER).orElseThrow();
            final MethodSpec childSpec = embedTransformer(field, MethodType.READ, childTransformer, ctx)
                    .toBuilder()
                    .setName(field.getFieldName() + "NotNull")
                    .returns(field.getTypeName())
                    .build();

            final MethodSpec method = defaultReadMethod(field, ctx)
                    .beginControlFlow("if (reader.readBoolean())")
                    .addStatement("return $L()", childSpec.name())
                    .endControlFlow()
                    .addStatement("return null")
                    .build();

            field.addGeneratedMethod(MethodType.WRITE, method);
            builder.addMethod(method);
            builder.addMethod(childSpec);
        }
    }
}
