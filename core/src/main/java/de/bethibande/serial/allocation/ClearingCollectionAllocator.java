package de.bethibande.serial.allocation;

import java.util.Collection;

/**
 * An implementation of {@link ParameterizedObjectAllocator} designed for re-using the same collection instance.
 * When calling {@link #allocate(Integer)} the collection is cleared using {@link Collection#clear()} and returned.
 *
 * @param <T> the type of collection managed by this allocator; must extend {@link Collection}
 */
public class ClearingCollectionAllocator<T extends Collection<?>> implements ParameterizedObjectAllocator<Integer, T> {

    private final T collection;

    public ClearingCollectionAllocator(final T collection) {
        this.collection = collection;
    }

    @Override
    public T allocate(final Integer parameter) {
        collection.clear();
        return collection;
    }
}
