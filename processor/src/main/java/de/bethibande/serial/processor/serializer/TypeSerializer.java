package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public interface TypeSerializer {

    /**
     * Converts the provided {@link AnnotatedConstruct} into a {@link TypeMirror}.
     * If the construct instance is of type {@link TypeMirror}, it is returned directly.
     * For {@link TypeElement}, {@link ExecutableElement}, and {@link RecordComponentElement},
     * their respective {@code asType()} or return types are retrieved and returned as a {@link TypeMirror}.
     *
     * @param construct the annotated construct to be converted into a {@link TypeMirror}
     * @return the {@link TypeMirror} corresponding to the input construct
     * @throws IllegalArgumentException if the construct type is not supported or recognized
     */
    default TypeMirror asTypeMirror(final AnnotatedConstruct construct) {
        return TypeHelper.resolveType(construct);
    }

    default Element asElement(final AnnotatedConstruct construct) {
        if (construct instanceof Element element) return element;
        throw new IllegalArgumentException("Unknown construct type: " + construct.getClass().getName());
    }

    boolean isApplicable(final AnnotatedConstruct construct, final ElementSerializer serializer);

    SizeCalculator size(final AnnotatedConstruct construct, final ElementSerializer serializer);

    CodeBlock write(final AnnotatedConstruct construct, final ElementSerializer serializer);

    CodeBlock read(final AnnotatedConstruct construct, final ElementSerializer serializer);

}
