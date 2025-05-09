package de.bethibande.serial.allocation;

@FunctionalInterface
public interface ParameterizedObjectAllocator<P, T> {

    T allocate(final P parameter);

}
