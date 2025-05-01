package de.bethibande.serial.processor.generator.deserializer;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.Reader;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;

import javax.lang.model.element.Modifier;

public class ReadMethodProcessor implements GenerationPostProcessor {

    @Override
    public void postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        final CodeBlock.Builder code = CodeBlock.builder();
        for (final FieldInfo field : context.fields()) {
            code.addStatement("$L.$L($L())", "target", field.getSetterName(), field.getGeneratedMethod(MethodType.READ).name());
        }

        builder.addMethod(MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(context.rawType(), "target")
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(code.build())
                .addStatement("return target")
                .returns(context.rawType())
                .build());
    }
}
