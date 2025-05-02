package de.bethibande.serial.impl;

import de.bethibande.serial.Serializer;
import de.bethibande.serial.Writer;

public abstract class AbstractSerializer<T> implements Serializer<T> {

    protected Writer writer;

    @Override
    public boolean isBound() {
        return writer != null;
    }

    @Override
    public Serializer<T> bind(final Writer writer) {
        this.writer = writer;
        return this;
    }
}
