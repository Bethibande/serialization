package de.bethibande.serial.processor.attributes;

import java.util.Optional;

public interface HasAttributes {

    <T> void set(final AttributeKey<T> key, final T value);

    <T> Optional<T> get(final AttributeKey<T> key);

}
