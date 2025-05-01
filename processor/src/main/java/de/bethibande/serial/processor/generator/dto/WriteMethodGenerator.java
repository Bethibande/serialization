package de.bethibande.serial.processor.generator.dto;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.Writer;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class WriteMethodGenerator implements GenerationPostProcessor {

    protected CodeBlock generateWritePart(final FieldInfo field, final ElementSerializer serializer) {
        return CodeBlock.builder()
                .add("// $N\n", field.getFieldName())
                .add(serializer.getSerializer(field.getGetter())
                        .orElseThrow()
                        .write(field.getGetter(), serializer))
                .build();
    }

    protected List<CodeBlock> generateWriteParts(final List<FieldInfo> fields, final ElementSerializer serializer) {
        final List<CodeBlock> parts = new ArrayList<>();

        parts.add(CodeBlock.builder()
                .addStatement("writer.markStart()")
                .build());

        parts.addAll(fields.stream()
                .map(it -> generateWritePart(it, serializer))
                .toList());

        parts.add(CodeBlock.builder()
                .addStatement("writer.markEnd()")
                .build());

        return parts;
    }

    @Override
    public TypeSpec.Builder postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        final List<CodeBlock> parts = generateWriteParts(context.fields(), context.serializer());
        final CodeBlock code = CodeBlock.join(parts, "\n");

        return builder.addMethod(MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(Writer.class, "writer")
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(code)
                .build());
    }

}
