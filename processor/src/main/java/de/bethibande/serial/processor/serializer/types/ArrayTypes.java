package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.EmbeddedTypeTransformer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

@AutoService(FieldBasedObjectTransformer.class)
public class ArrayTypes extends EmbeddedTypeTransformer {

    @Override
    protected boolean isApplicable0(final FieldInfo field) {
        return !field.isNullable() && field.getType().getKind() == TypeKind.ARRAY;
    }

    @Override
    protected FieldInfo createChildType(final FieldInfo field, final ElementSerializer serializer) {
        final TypeMirror newType = unwrapArrayType(field.getType());
        return field.toBuilder()
                .type(newType)
                .nullable(TypeHelper.isNullable(newType))
                .build();
    }

    private TypeMirror unwrapArrayType(final TypeMirror type) {
        if (!(type instanceof ArrayType arrayType)) throw new IllegalArgumentException("Type is not an array");
        return arrayType.getComponentType();
    }

    @Override
    public Optional<String> wrappedMethodNameSuffix() {
        return Optional.of("ArrayValue");
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("$L.writeInt($L.length)", "writer", "value")
                .beginControlFlow("for ($T item : $L)", field.getFirstChild().getType(), "value")
                .addStatement("$L(item)", embeddedMethodName(field))
                .endControlFlow()
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement("final $T $L = new $T[$L.readInt()]", field.getType(), "value", field.getFirstChild().getType(), "reader")
                .beginControlFlow("for (int i = 0; i < $L.length; i++)", "value")
                .addStatement("$L[i] = $L()", "value", embeddedMethodName(field))
                .endControlFlow()
                .addStatement("return $L", "value")
                .build();
    }
}
