package de.bethibande.serial.processor.generator;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.Generator;
import de.bethibande.serial.processor.context.SerializationContext;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenerator implements Generator {

    private final List<GenerationPostProcessor> postProcessors = new ArrayList<>();

    @Override
    public void generate(final SerializationContext context) throws IOException {
        if (!this.shouldGenerate(context)) return;

        final TypeSpec.Builder builder = this.generateType(context);
        for (final GenerationPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcess(builder, context);
        }

        final TypeSpec type = builder.build();
        final ClassName generatedType = typeName(context);
        final JavaFile file = JavaFile.builder(generatedType.packageName(), type)
                .build();
        final JavaFileObject fileObject = context.processingEnv()
                .getFiler()
                .createSourceFile(generatedType.toString(), context.type());

        try (final Writer writer = fileObject.openWriter()) {
            file.writeTo(writer);
        }
    }

    public abstract ClassName typeName(final SerializationContext context);

    protected boolean shouldGenerate(final SerializationContext context) {
        return true;
    }

    public abstract TypeSpec.Builder generateType(final SerializationContext context);

    @Override
    public Generator withPostProcessor(final GenerationPostProcessor processor) {
        this.postProcessors.add(processor);
        return this;
    }
}
