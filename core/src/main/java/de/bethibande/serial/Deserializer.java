package de.bethibande.serial;

import de.bethibande.serial.allocation.ObjectAllocator;

public interface Deserializer<T> {

    boolean isBound();

    Deserializer<T> bind(final Reader reader);

    Deserializer<T> withAllocator(final ObjectAllocator<T> allocator);

    ObjectAllocator<T> allocator();

    default T read() {
        return read(allocator().allocate());
    }

    T read(final T target);

}
