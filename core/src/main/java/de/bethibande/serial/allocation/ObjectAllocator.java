package de.bethibande.serial.allocation;

@FunctionalInterface
public interface ObjectAllocator<T> {

    static <T> ObjectAllocator<T> ofStaticValue(final T instance) {
        return () -> instance;
    }

    T allocate();

}
