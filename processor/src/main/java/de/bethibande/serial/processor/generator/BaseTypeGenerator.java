package de.bethibande.serial.processor.generator;

import com.palantir.javapoet.*;
import de.bethibande.serial.processor.context.SerializationContext;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

public abstract class BaseTypeGenerator extends AbstractGenerator {

    protected FieldSpec buildField(final FieldInfo field) {
        final TypeName type = TypeName.get(field.getType());

        return FieldSpec.builder(type, field.getFieldName())
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    protected CodeBlock getterCode(final FieldInfo field) {
        return CodeBlock.of("return this.$N;", field.getFieldName());
    }

    protected MethodSpec buildGetter(final FieldInfo field) {
        final TypeName type = TypeName.get(field.getType());

        return MethodSpec.methodBuilder(field.getGetterName())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addCode(getterCode(field))
                .returns(type)
                .build();
    }

    protected List<AnnotationSpec> overrideAnnotationOrEmpty(final ExecutableElement element) {
        return element != null ? List.of(AnnotationSpec.builder(Override.class).build()) : Collections.emptyList();
    }

    protected CodeBlock setterCode(final FieldInfo field) {
        return CodeBlock.of("this.$N = $N;", field.getFieldName(), field.getFieldName());
    }

    protected MethodSpec buildSetter(final FieldInfo field) {
        final TypeName type = TypeName.get(field.getType());
        final List<AnnotationSpec> annotations = overrideAnnotationOrEmpty(field.getSetter());

        return MethodSpec.methodBuilder(field.getSetterName())
                .addAnnotations(annotations)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(type, field.getFieldName())
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(setterCode(field))
                .build();
    }

    protected CodeBlock fluentSetterCode(final FieldInfo field) {
        return CodeBlock.builder()
                .addStatement("this.$N = $N", field.getFieldName(), field.getFieldName())
                .addStatement("return this")
                .build();
    }

    protected MethodSpec buildFluentSetter(final FieldInfo field, final TypeName returnType) {
        final TypeName type = TypeName.get(field.getType());
        final List<AnnotationSpec> annotations = overrideAnnotationOrEmpty(field.getFluentSetter());

        return MethodSpec.methodBuilder(field.getFluentSetterName())
                .addAnnotations(annotations)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(type, field.getFieldName())
                        .addModifiers(Modifier.FINAL)
                        .build())
                .addCode(fluentSetterCode(field))
                .returns(returnType)
                .build();
    }

    private List<FieldSpec> buildFields(final List<FieldInfo> fields) {
        return fields.stream()
                .map(this::buildField)
                .toList();
    }

    private List<MethodSpec> buildGetters(final List<FieldInfo> fields) {
        return fields.stream()
                .map(this::buildGetter)
                .toList();
    }

    private List<MethodSpec> buildSetters(final List<FieldInfo> fields) {
        return fields.stream()
                .map(this::buildSetter)
                .toList();
    }

    private List<MethodSpec> buildFluentSetters(final List<FieldInfo> fields, final TypeName returnType) {
        return fields.stream()
                .map(it -> buildFluentSetter(it, returnType))
                .toList();
    }

    @Override
    public TypeSpec.Builder generateType(final SerializationContext context) {
        return TypeSpec.classBuilder(typeName(context))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(context.rawType())
                .addFields(buildFields(context.fields()))
                .addMethods(buildGetters(context.fields()))
                .addMethods(buildSetters(context.fields()))
                .addMethods(buildFluentSetters(context.fields(), typeName(context)));
    }
}
