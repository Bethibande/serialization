package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public interface FieldBasedObjectTransformer {

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

    boolean isApplicable(final FieldInfo field, final ElementSerializer serializer);

    default Optional<String> wrappedMethodNameSuffix() {
        return Optional.empty();
    }

    default void forEachSingleChild(final FieldInfo root,
                                    final Function<FieldInfo, FieldInfo> childFunction,
                                    final Consumer<FieldInfo> consumer) {
        forEach(root, field -> {
            final FieldInfo child = childFunction.apply(field);
            if (child == null) return Collections.emptyList();
            return Collections.singletonList(child);
        }, consumer);
    }

    default void forEach(final FieldInfo root,
                         final Function<FieldInfo, Collection<FieldInfo>> childFunction,
                         final Consumer<FieldInfo> consumer) {
        final Stack<FieldInfo> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            final FieldInfo current = stack.pop();
            if (current == null) continue;

            consumer.accept(current);
            childFunction.apply(current).forEach(stack::push);
        }
    }

    default <T> List<T> mapEach(final FieldInfo root,
                                final Function<FieldInfo, Collection<FieldInfo>> childFunction,
                                final Function<FieldInfo, T> consumer) {
        final List<T> values = new ArrayList<>();
        forEach(root, childFunction, (current) -> values.add(consumer.apply(current)));

        return values;
    }

    default String methodName(final FieldInfo field) {
        final StringBuilder builder = new StringBuilder(field.getFieldName());
        if (field.getParent() != null) {
            forEachSingleChild(
                    field.getParent(),
                    FieldInfo::getParent,
                    parent -> parent.getTransformer()
                            .wrappedMethodNameSuffix()
                            .ifPresent(builder::append)
            );
        }

        return builder.toString();
    }

    CodeBlock createSerializationCode(final FieldInfo field,
                                      final SerializationContext ctx);

    CodeBlock createDeserializationCode(final FieldInfo field,
                                        final SerializationContext ctx);

    default void applySerializerTransformation(final TypeSpec.Builder builder,
                                               final FieldInfo field,
                                               final SerializationContext ctx) {
    }

    default MethodSpec createSerializationMethod(final FieldInfo field,
                                                 final SerializationContext ctx) {
        return MethodSpec.methodBuilder(methodName(field))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(field.getTypeName(), "value")
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(createSerializationCode(field, ctx))
                .returns(ctx.serializerType())
                .build();
    }

    default void applyDeserializerTransformation(final TypeSpec.Builder builder,
                                                 final FieldInfo field,
                                                 final SerializationContext ctx) {
    }

    default MethodSpec createDeserializationMethod(final FieldInfo field,
                                                   final SerializationContext ctx) {
        return MethodSpec.methodBuilder(methodName(field))
                .addModifiers(Modifier.PUBLIC)
                .addCode(createDeserializationCode(field, ctx))
                .returns(field.getTypeName())
                .build();
    }

    default void transformType(final TypeSpec.Builder builder,
                               final FieldInfo field,
                               final MethodType methodType,
                               final SerializationContext ctx) {
        final List<MethodSpec> methods = mapEach(
                field,
                FieldInfo::getChildren,
                child -> {
                    final FieldBasedObjectTransformer transformer = child.getTransformer();
                    final MethodSpec method = switch (methodType) {
                        case SERIALIZE -> transformer.createSerializationMethod(child, ctx);
                        case DESERIALIZE -> transformer.createDeserializationMethod(child, ctx);
                    };

                    switch (methodType) {
                        case SERIALIZE -> transformer.applySerializerTransformation(builder, child, ctx);
                        case DESERIALIZE -> transformer.applyDeserializerTransformation(builder, child, ctx);
                    }

                    child.addGeneratedMethod(methodType, method);
                    return method;
                }
        );

        builder.addMethods(methods);
    }
}
