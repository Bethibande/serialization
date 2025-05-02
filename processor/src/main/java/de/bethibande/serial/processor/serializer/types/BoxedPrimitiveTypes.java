package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import de.bethibande.serial.processor.serializer.Operations;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@AutoService(FieldBasedObjectTransformer.class)
public class BoxedPrimitiveTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && TypeHelper.isBoxedPrimitive(field.getType());
    }

    private TypeKind toPrimitiveType(final TypeMirror type) {
        return TypeHelper.unbox(type).getKind();
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        if (!TypeHelper.isSuppressed(field.getField(), "boxedtypes") && !field.isNullable()) {
            ctx.processingEnv()
                    .getMessager()
                    .printWarning("Usage of boxed types is not recommended for non-nullable primitives. You can suppress this warning using @SuppressWarnings(\"boxedtypes\")", field.getField());
        }

        final TypeKind kind = this.toPrimitiveType(field.getType());

        return CodeBlock.builder()
                .addStatement(Operations.writeOperation(kind), "target", field.getFieldName())
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        final TypeKind kind = this.toPrimitiveType(field.getType());

        return CodeBlock.builder()
                .addStatement(Operations.readOperation(kind), "reader")
                .build();
    }
}
