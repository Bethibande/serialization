package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import de.bethibande.serial.allocation.ParameterizedObjectAllocator;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.EmbeddedTypeTransformer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.Optional;

@AutoService(FieldBasedObjectTransformer.class)
public class CollectionTypes extends EmbeddedTypeTransformer {

    @Override
    protected boolean isApplicable0(final FieldInfo field) {
        return !field.isNullable() && TypeHelper.isAssignable(TypeHelper.erasure(field.getType()), TypeHelper.asType(Collection.class));
    }

    @Override
    protected FieldInfo createChildType(final FieldInfo field, final ElementSerializer serializer) {
        if (field.getType() instanceof DeclaredType declaredType) {
            final TypeMirror type = declaredType.getTypeArguments().getFirst();

            return field.toBuilder()
                    .type(type)
                    .nullable(TypeHelper.isNullable(type))
                    .build();
        }
        throw new IllegalArgumentException("Type is not a declared type");
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("$L.writeInt($L.size())", FIELD_WRITER, FIELD_VALUE)
                .beginControlFlow("for ($T item : $L)", field.getFirstChild().getType(), FIELD_VALUE)
                .addStatement("$L(item)", embeddedMethodName(field))
                .endControlFlow()
                .addStatement("return this")
                .build();
    }

    protected String allocatorFieldName(final FieldInfo field) {
        return methodName(field) + "Allocator";
    }

    @Override
    public Optional<String> wrappedMethodNameSuffix() {
        return Optional.of("CollectionValue");
    }

    @Override
    public void applyDeserializerTransformation(final TypeSpec.Builder builder,
                                                final FieldInfo field,
                                                final SerializationContext ctx) {
        final TypeName fieldType = TypeName.get(field.getType());
        final TypeName allocatorType = ParameterizedTypeName.get(
                ClassName.get(ParameterizedObjectAllocator.class),
                ClassName.get(Integer.class),
                fieldType
        );
        final String allocatorFieldName = allocatorFieldName(field);

        final FieldSpec allocatorField = FieldSpec.builder(allocatorType, allocatorFieldName)
                .addModifiers(Modifier.PRIVATE)
                .build();

        final MethodSpec allocatorMethod = MethodSpec.methodBuilder(allocatorFieldName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(allocatorType, allocatorFieldName)
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addStatement("this.$L = $L", allocatorFieldName, allocatorFieldName)
                .addStatement("return this")
                .returns(ctx.deserializerType())
                .build();

        builder.addField(allocatorField);
        builder.addMethod(allocatorMethod);
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("final int length = $L.readInt()", FIELD_READER)
                .addStatement("final $T $L = $L.allocate(length)", field.getType(), FIELD_VALUE, allocatorFieldName(field))
                .beginControlFlow("for (int i = 0; i < length; i++)")
                .addStatement("$L.add($L())", FIELD_VALUE, embeddedMethodName(field))
                .endControlFlow()
                .addStatement("return $L", FIELD_VALUE)
                .build();
    }
}
