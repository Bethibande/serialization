package de.bethibande.serial.processor;

import de.bethibande.serial.processor.context.SerializationContext;

import java.io.IOException;

public interface Generator {

    void generate(final SerializationContext context) throws IOException;

    Generator withPostProcessor(final GenerationPostProcessor processor);

}
