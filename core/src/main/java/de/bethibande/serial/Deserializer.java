package de.bethibande.serial;

public interface Deserializer<T> {

    boolean isBound();

    Deserializer<T> bind(final Reader reader);

    T read(final T target);

}
