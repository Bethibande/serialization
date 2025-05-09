package de.bethibande.serial.allocation;

/**
 * A thread-safe object allocator implementation that uses {@link ThreadLocal} to provide
 * a unique instance of the allocated object per thread.
 *
 * @param <T> the type of object to be allocated
 */
public class ThreadLocalObjectAllocator<T> implements ObjectAllocator<T> {

    private final ThreadLocal<T> holder;

    public ThreadLocalObjectAllocator(final ObjectAllocator<T> allocator) {
        this.holder = ThreadLocal.withInitial(allocator::allocate);
    }

    @Override
    public T allocate() {
        return holder.get();
    }

    public void deallocate() {
        holder.remove();
    }

}
