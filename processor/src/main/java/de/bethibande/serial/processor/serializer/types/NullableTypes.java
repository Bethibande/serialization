package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.EmbeddedTypeTransformer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import java.util.Optional;

@AutoService(FieldBasedObjectTransformer.class)
public class NullableTypes extends EmbeddedTypeTransformer {

    @Override
    protected boolean isApplicable0(final FieldInfo field) {
        return field.isNullable();
    }

    @Override
    protected FieldInfo createChildType(final FieldInfo field, final ElementSerializer serializer) {
        return field.toBuilder()
                .nullable(false)
                .build();
    }

    @Override
    public Optional<String> wrappedMethodNameSuffix() {
        return Optional.of("NotNull");
    }

    @Override
    public CodeBlock createSerializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if (value != null)")
                .addStatement("$L.writeBoolean(true)", FIELD_WRITER)
                .addStatement("$L(value)", embeddedMethodName(field))
                .addStatement("return this")
                .endControlFlow()
                .addStatement("$L.writeBoolean(false)", FIELD_WRITER)
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializationCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .beginControlFlow("if ($L.readBoolean())", FIELD_READER)
                .addStatement("return $L()", embeddedMethodName(field))
                .endControlFlow()
                .addStatement("return null")
                .build();
    }
}
