package de.bethibande.serial.processor;

import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.context.SerializationContext;

public interface GenerationPostProcessor {

    void postProcess(final TypeSpec.Builder builder, final SerializationContext context);

}
