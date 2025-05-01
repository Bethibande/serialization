package de.bethibande.serial.processor.generator.dto;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.SizeCalculator;

import javax.lang.model.element.Modifier;
import java.util.List;

public class SizeMethodGenerator implements GenerationPostProcessor {

    protected SizeCalculator toSize(final FieldInfo field, final ElementSerializer serializer) {
        return serializer.getSerializer(field.getGetter())
                .orElseThrow()
                .size(field.getGetter(), serializer);
    }

    protected List<SizeCalculator> toSizes(final List<FieldInfo> fields, final ElementSerializer serializer) {
        return fields.stream()
                .map(it -> toSize(it, serializer))
                .toList();
    }

    @Override
    public TypeSpec.Builder postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        final List<SizeCalculator> calculators = toSizes(context.fields(), context.serializer());
        final SizeCalculator summedCalculator = SizeCalculator.ofAll(calculators);

        final CodeBlock block = CodeBlock.builder()
                .add("return ")
                .addStatement(summedCalculator.toCodeBlock())
                .build();

        return builder.addMethod(MethodSpec.methodBuilder("size")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addCode(block)
                .returns(int.class)
                .build());
    }

}
