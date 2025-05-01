package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.attributes.AttributeKey;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import java.util.Collection;

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

    private MethodSpec embedSerializer(final FieldInfo field,
                                       final FieldBasedObjectTransformer transformer,
                                       final SerializationContext ctx) {
        return defaultWriteMethod(field, field.getFieldName() + "NotNull", ctx)
                .addCode(transformer.createSerializerCode(field, ctx))
                .build();
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if ($L != null)", field.getFieldName())
                .addStatement("target.writeBoolean(true)")
                .addStatement("return $L($L)", field.getFieldName() + "NotNull", field.getFieldName())
                .endControlFlow()
                .addStatement("target.writeBoolean(false)")
                .addStatement("return this")
                .build();
    }

    @Override
    public void transformSerializer(final TypeSpec.Builder builder,
                                    final Collection<FieldInfo> fields,
                                    final SerializationContext ctx) {
        for (final FieldInfo field : fields) {
            final FieldBasedObjectTransformer childTransformer = field.get(CHILD_TRANSFORMER).orElseThrow();
            final MethodSpec childSpec = embedSerializer(field, childTransformer, ctx);
            final MethodSpec method = createSerializerMethod(field, ctx);

            field.addGeneratedMethod(MethodType.WRITE, method);
            builder.addMethod(method);
            builder.addMethod(childSpec);
        }
    }

    private MethodSpec embedDeserializer(final FieldInfo field,
                                         final FieldBasedObjectTransformer transformer,
                                         final SerializationContext ctx) {
        return defaultReadMethod(field, field.getFieldName() + "NotNull", ctx)
                .addCode(transformer.createDeserializerCode(field, ctx))
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if (reader.readBoolean())")
                .addStatement("return $L()", field.getFieldName() + "NotNull")
                .endControlFlow()
                .addStatement("return null")
                .build();
    }

    @Override
    public void transformDeserializer(final TypeSpec.Builder builder,
                                      final Collection<FieldInfo> fields,
                                      final SerializationContext ctx) {
        for (final FieldInfo field : fields) {
            final FieldBasedObjectTransformer childTransformer = field.get(CHILD_TRANSFORMER).orElseThrow();
            final MethodSpec childSpec = embedDeserializer(field, childTransformer, ctx);
            final MethodSpec method = createDeserializerMethod(field, ctx);

            field.addGeneratedMethod(MethodType.READ, method);
            builder.addMethod(method);
            builder.addMethod(childSpec);
        }
    }
}
