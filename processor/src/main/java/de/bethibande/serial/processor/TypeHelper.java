package de.bethibande.serial.processor;

import com.palantir.javapoet.ClassName;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class TypeHelper {

    /**
     * Resolves the {@link TypeMirror} representation of a given {@link AnnotatedConstruct}.
     * The method determines the type of the provided construct and returns its associated type.
     *
     * @param construct the annotated construct whose type needs to be resolved.
     *                  Must be an instance of {@link TypeMirror}, {@link TypeElement},
     *                  {@link ExecutableElement}, or {@link RecordComponentElement}.
     * @return the resolved {@link TypeMirror} corresponding to the construct's type.
     * @throws IllegalArgumentException if the construct is of an unknown or unsupported type.
     */
    public static TypeMirror resolveType(final AnnotatedConstruct construct) {
        if (construct instanceof TypeMirror typeMirror) return typeMirror;
        if (construct instanceof TypeElement element) return element.asType();
        if (construct instanceof ExecutableElement executable) return executable.getReturnType();
        if (construct instanceof RecordComponentElement recordComponent) return recordComponent.asType();
        throw new IllegalArgumentException("Unknown construct type: " + construct.getClass().getName());
    }

    public static ClassName getDTOName(final ClassName rawType) {
        return ClassName.get(rawType.packageName(), rawType.simpleName() + "DTO");
    }

    public static ClassName getSnapshotName(final ClassName rawType) {
        return ClassName.get(rawType.packageName(), rawType.simpleName() + "Snapshot");
    }

    public static String toFieldName(final String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String capitalizeName(final String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getFieldName(final Element element) {
        final String name = element.getSimpleName().toString();

        if (name.startsWith("is")) return toFieldName(name.substring(2));
        if (name.startsWith("get")) return toFieldName(name.substring(3));
        if (name.startsWith("set")) return toFieldName(name.substring(3));
        return toFieldName(name);
    }

    public static boolean isFluentSetter(final ExecutableElement element) {
        return isSetter(element) && element.getReturnType().getKind() != TypeKind.VOID;
    }

    public static boolean isSetter(final ExecutableElement element) {
        return element.getParameters().size() == 1 && element.getReturnType().getKind() == TypeKind.VOID;
    }

}
