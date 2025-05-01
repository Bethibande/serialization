package de.bethibande.serial;

public interface Serializer<T> {

    boolean isBound();

    Serializer<T> bind(final Writer target);

    Serializer<T> write(final T value);

}
