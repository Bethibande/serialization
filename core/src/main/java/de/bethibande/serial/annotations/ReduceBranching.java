package de.bethibande.serial.annotations;

import de.bethibande.serial.HasSnapshots;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Tell the processor to minimize branching in the generated read and write methods.
 * This comes at the cost of larger serialized sizes, optimizing purely for CPU performance.
 * It primarily does so by not only avoiding some branching all together,
 * but also by moving some branches to less sensitive areas, such as the getter and setter methods.
 * This annotation is only applicable to interfaces annotated with {@link SerializableType}.
 * <p>
 * All classes generated with this annotation will implement the {@link HasSnapshots} interface.
 * <p>
 * Please note that you should properly benchmark the performance gain of this optimization.
 * </p>
 * @see SerializableType
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ReduceBranching {
}
