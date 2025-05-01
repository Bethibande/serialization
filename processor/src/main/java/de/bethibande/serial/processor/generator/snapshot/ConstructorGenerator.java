package de.bethibande.serial.processor.generator.snapshot;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;

import javax.lang.model.element.Modifier;
import java.util.List;

public class ConstructorGenerator implements GenerationPostProcessor {

    private List<ParameterSpec> generateParameters(final List<FieldInfo> fields) {
        return fields.stream()
                .map(it -> ParameterSpec.builder(it.getTypeName(), it.getFieldName())
                        .addModifiers(Modifier.FINAL)
                        .build())
                .toList();
    }

    private CodeBlock generateInitializer(final List<FieldInfo> fields) {
        final CodeBlock.Builder code = CodeBlock.builder();
        for (final FieldInfo field : fields) {
            code.addStatement("this.$N = $N", field.getFieldName(), field.getFieldName());
        }
        return code.build();
    }

    private MethodSpec generateConstructor(final SerializationContext context) {
        return MethodSpec.constructorBuilder()
                .addParameters(generateParameters(context.fields()))
                .addCode(generateInitializer(context.fields()))
                .build();
    }

    @Override
    public TypeSpec.Builder postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        return builder.addMethod(MethodSpec.constructorBuilder().build())
                .addMethod(generateConstructor(context));
    }
}
