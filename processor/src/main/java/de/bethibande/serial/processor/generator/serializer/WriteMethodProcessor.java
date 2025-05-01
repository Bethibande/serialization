package de.bethibande.serial.processor.generator.serializer;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;

import javax.lang.model.element.Modifier;

public class WriteMethodProcessor implements GenerationPostProcessor {

    @Override
    public void postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        final CodeBlock.Builder code = CodeBlock.builder();
        for (final FieldInfo field : context.fields()) {
            code.addStatement("$L($L.$L())", field.getGeneratedMethod(MethodType.WRITE).name(), "value", field.getGetterName());
        }

        builder.addMethod(MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(context.rawType(), "value")
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(code.build())
                .addStatement("return this")
                .returns(context.serializerType())
                .build());
    }
}
