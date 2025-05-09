package de.bethibande.serial.impl;

import de.bethibande.serial.Deserializer;
import de.bethibande.serial.Reader;
import de.bethibande.serial.allocation.ObjectAllocator;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {

    protected Reader reader;
    protected ObjectAllocator<T> allocator;
    
    @Override
    public boolean isBound() {
        return reader != null;
    }

    @Override
    public Deserializer<T> bind(final Reader reader) {
        this.reader = reader;
        return this;
    }

    @Override
    public Deserializer<T> withAllocator(final ObjectAllocator<T> allocator) {
        this.allocator = allocator;
        return this;
    }

    @Override
    public ObjectAllocator<T> allocator() {
        return allocator;
    }
}
