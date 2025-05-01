package de.bethibande.serial.impl;

import de.bethibande.serial.Deserializer;
import de.bethibande.serial.Reader;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {

    protected Reader reader;
    
    @Override
    public boolean isBound() {
        return reader != null;
    }

    @Override
    public Deserializer<T> bind(final Reader reader) {
        this.reader = reader;
        return this;
    }
}
