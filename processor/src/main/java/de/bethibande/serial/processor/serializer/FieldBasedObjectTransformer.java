package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;

public interface FieldBasedObjectTransformer {

    default MethodSpec.Builder defaultWriteMethod(final FieldInfo field, final SerializationContext ctx) {
        return MethodSpec.methodBuilder(field.getFieldName())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(field.getTypeName(), field.getFieldName())
                        .addModifiers(Modifier.FINAL)
                        .build())
                .returns(ctx.serializerType());
    }

    default MethodSpec.Builder defaultReadMethod(final FieldInfo field, final SerializationContext ctx) {
        return MethodSpec.methodBuilder(field.getFieldName())
                .addModifiers(Modifier.PUBLIC)
                .returns(field.getTypeName());
    }

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

    void transformSerializer(final TypeSpec.Builder builder,
                             final Collection<FieldInfo> fields,
                             final SerializationContext ctx);

    void transformDeserializer(final TypeSpec.Builder builder,
                               final Collection<FieldInfo> fields,
                               final SerializationContext ctx);
}
