package de.bethibande.serial.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

public class TypeHelper {

    private static final Set<String> NOT_NULL_ANNOTATIONS = Set.of(
            "notnull",
            "notnullable",
            "nonnull",
            "nonnullable"
    );

    public static TypeElement asElement(final TypeMirror type) {
        return SerializationProcessor.ELEMENTS.getTypeElement(TypeName.get(type).withoutAnnotations().toString());
    }

    public static TypeElement asElement(final Class<?> clazz) {
        return SerializationProcessor.ELEMENTS.getTypeElement(clazz.getCanonicalName());
    }

    public static TypeMirror asType(final Class<?> clazz) {
        return asElement(clazz).asType();
    }

    public static boolean isCharSequence(final TypeMirror type) {
        return SerializationProcessor.TYPES.isSubtype(type, asType(CharSequence.class));
    }

    public static boolean isAssignable(final TypeMirror superType, final TypeMirror to) {
        return SerializationProcessor.TYPES.isAssignable(superType, to);
    }

    public static boolean hasNotNullAnnotation(final TypeMirror type) {
        return type.getAnnotationMirrors()
                .stream()
                .map(it -> it.getAnnotationType().asElement().getSimpleName().toString())
                .map(String::toLowerCase)
                .anyMatch(NOT_NULL_ANNOTATIONS::contains);
    }

    public static boolean isNullable(final TypeMirror type) {
        return !isNotNull(type);
    }

    public static boolean isNotNull(final TypeMirror type) {
        return type.getKind().isPrimitive() || hasNotNullAnnotation(type);
    }

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

    public static ClassName adaptTypeName(final ClassName rawType, final String suffix) {
        return ClassName.get(rawType.packageName(), rawType.simpleName() + suffix);
    }

    public static String toFieldName(final String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String capitalizeName(final String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getFieldName(final Element element) {
        final String name = element.getSimpleName().toString();

        if (name.startsWith("with")) return toFieldName(name.substring(4));
        if (name.startsWith("is")) return toFieldName(name.substring(2));
        if (name.startsWith("get")) return toFieldName(name.substring(3));
        if (name.startsWith("set")) return toFieldName(name.substring(3));
        return toFieldName(name);
    }

    public static boolean isFluentSetter(final ExecutableElement element) {
        return element.getParameters().size() == 1 && element.getReturnType().getKind() != TypeKind.VOID;
    }

    public static boolean isSetter(final ExecutableElement element) {
        return element.getParameters().size() == 1 && element.getReturnType().getKind() == TypeKind.VOID;
    }

    public static boolean isGetter(final ExecutableElement element) {
        return element.getParameters().isEmpty() && element.getReturnType().getKind() != TypeKind.VOID;
    }

}
