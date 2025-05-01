package de.bethibande.serial;

/**
 * Represents an interface for objects that support creating snapshots of their state
 * and restoring state from a given snapshot.
 * <p>
 * Classes implementing this interface can provide the ability to save their current
 * state into a snapshot instance and later revert to this state, facilitating state
 * management and rollback operations.
 *
 * @param <S> the type that represents the snapshot for the object's state
 */
public interface HasSnapshots<S> {

    /**
     * Creates and returns a snapshot of the current state.
     * The implementation used for the snapshot is intended only for get and set access.
     *
     * @return an instance representing a snapshot of the current state
     */
    S snapshot();

    /**
     * Restores the state of the object from the provided snapshot.
     *
     * @param snapshot the snapshot instance containing the state to restore
     */
    void restore(final S snapshot);

}
