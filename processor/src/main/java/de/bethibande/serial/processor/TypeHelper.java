package de.bethibande.serial.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.*;
import javax.lang.model.type.PrimitiveType;
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

    private static final Set<String> BOXED_PRIMITIVE_TYPES = Set.of(
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Float",
            "java.lang.Double",
            "java.lang.Boolean",
            "java.lang.Character"
    );

    public static boolean isSuppressed(final AnnotatedConstruct construct, final String warning) {
        final SuppressWarnings suppressWarnings = construct.getAnnotation(SuppressWarnings.class);
        if (suppressWarnings == null) return false;

        return Set.of(suppressWarnings.value()).contains(warning);
    }

    public static PrimitiveType unbox(final TypeMirror type) {
        return SerializationProcessor.TYPES.get().unboxedType(type);
    }

    public static String rawType(final TypeMirror type) {
        return TypeName.get(type).withoutAnnotations().toString();
    }

    public static boolean isBoxedPrimitive(final TypeMirror type) {
        return BOXED_PRIMITIVE_TYPES.contains(rawType(type));
    }

    public static TypeElement asElement(final TypeMirror type) {
        return SerializationProcessor.ELEMENTS.get().getTypeElement(rawType(type));
    }

    public static TypeElement asElement(final Class<?> clazz) {
        return SerializationProcessor.ELEMENTS.get().getTypeElement(clazz.getCanonicalName());
    }

    public static TypeMirror asType(final Class<?> clazz) {
        return asElement(clazz).asType();
    }

    public static TypeMirror erasure(final TypeMirror type) {
        return SerializationProcessor.TYPES.get().erasure(type);
    }

    public static boolean is(final TypeMirror a, final TypeMirror b) {
        return SerializationProcessor.TYPES.get().isSameType(a, b);
    }

    public static boolean isCharSequence(final TypeMirror type) {
        return SerializationProcessor.TYPES.get().isSubtype(type, asType(CharSequence.class));
    }

    public static boolean isAssignable(final TypeMirror superType, final TypeMirror to) {
        return SerializationProcessor.TYPES.get().isAssignable(superType, to);
    }

    public static boolean hasNotNullAnnotation(final AnnotatedConstruct construct) {
        return construct.getAnnotationMirrors()
                .stream()
                .map(it -> it.getAnnotationType().asElement().getSimpleName().toString())
                .map(String::toLowerCase)
                .anyMatch(NOT_NULL_ANNOTATIONS::contains);
    }

    public static boolean isNullable(final AnnotatedConstruct construct) {
        return !isNotNull(construct);
    }

    public static boolean isNullable(final TypeMirror type) {
        return !isNotNull(type);
    }

    public static boolean isNotNull(final AnnotatedConstruct construct) {
        return resolveType(construct).getKind().isPrimitive() || hasNotNullAnnotation(construct);
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
        if (construct instanceof VariableElement variableElement) return variableElement.asType();
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

    private TypeHelper() {}

}
