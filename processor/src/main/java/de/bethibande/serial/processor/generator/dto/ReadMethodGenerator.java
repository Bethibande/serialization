package de.bethibande.serial.processor.generator.dto;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.Reader;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;

import javax.lang.model.element.Modifier;
import java.util.List;

public class ReadMethodGenerator implements GenerationPostProcessor {

    protected CodeBlock generateReadPart(final FieldInfo field, final ElementSerializer serializer) {
        return CodeBlock.builder()
                .add("// $N\n", field.getFieldName())
                .add(serializer.getSerializer(field.getGetter())
                        .orElseThrow()
                        .read(field.getGetter(), serializer))
                .build();
    }

    protected List<CodeBlock> generateReadParts(final List<FieldInfo> fields, final ElementSerializer serializer) {
        return fields.stream()
                .map(it -> generateReadPart(it, serializer))
                .toList();
    }

    @Override
    public TypeSpec.Builder postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        final List<CodeBlock> parts = generateReadParts(context.fields(), context.serializer());
        final CodeBlock code = CodeBlock.join(parts, "\n");

        return builder.addMethod(MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(Reader.class, "reader")
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(code)
                .build());
    }

}
