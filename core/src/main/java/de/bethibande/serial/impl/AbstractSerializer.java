package de.bethibande.serial.impl;

import de.bethibande.serial.Serializer;
import de.bethibande.serial.Writer;

public abstract class AbstractSerializer<T> implements Serializer<T> {

    protected Writer target;

    @Override
    public boolean isBound() {
        return target != null;
    }

    @Override
    public Serializer<T> bind(final Writer target) {
        this.target = target;
        return this;
    }
}
