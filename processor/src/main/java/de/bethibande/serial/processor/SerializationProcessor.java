package de.bethibande.serial.processor;

import com.google.auto.service.AutoService;
import de.bethibande.serial.annotations.SerializableType;
import de.bethibande.serial.processor.context.SerializationConfig;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_23)
public class SerializationProcessor extends AbstractProcessor {

    public static Types TYPES;
    public static Elements ELEMENTS;

    private final List<Generator> generators = load(Generator.class);
    private final ElementSerializer serializer;

    private <T> List<T> load(final Class<T> clazz) {
        final List<T> values = new ArrayList<>();
        ServiceLoader.load(clazz).forEach(values::add);
        ServiceLoader.load(clazz, SerializationProcessor.class.getClassLoader()).forEach(values::add);
        return values;
    }

    public SerializationProcessor() {
        final List<FieldBasedObjectTransformer> serializers = load(FieldBasedObjectTransformer.class);
        this.serializer = new ElementSerializer(serializers);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;

        TYPES = processingEnv.getTypeUtils();
        ELEMENTS = processingEnv.getElementUtils();

        final List<TypeElement> types = roundEnv.getElementsAnnotatedWith(SerializableType.class)
                .stream()
                .filter(it -> it.getKind().isClass())
                .map(TypeElement.class::cast)
                .toList();

        for (final TypeElement type : types) {
            generate(type, roundEnv);
        }

        return false;
    }

    private void logUnknownType(final FieldInfo field) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "Unknown type: " + field.getType(),
                field.getGetter()
        );
    }

    private void findAccessors(final TypeElement parent, final FieldInfo field) {
        final TypeMirror fieldType = field.getType();
        final TypeMirror parentType = parent.asType();

        for (final Element enclosedElement : parent.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) continue;

            final ExecutableElement method = (ExecutableElement) enclosedElement;
            final String name = TypeHelper.getFieldName(method);
            if (!name.equals(field.getFieldName())) continue;

            final TypeMirror returnType = method.getReturnType();

            if (TypeHelper.isFluentSetter(method)
                    && TypeHelper.isAssignable(returnType, parentType)
                    && TypeHelper.isAssignable(method.getParameters().getFirst().asType(), fieldType)) {
                field.setFluentSetter(method);
            } else if (TypeHelper.isSetter(method)
                    && TypeHelper.isAssignable(method.getParameters().getFirst().asType(), fieldType)) {
                field.setSetter(method);
            } else if (TypeHelper.isGetter(method) && TypeHelper.isAssignable(returnType, fieldType)) {
                field.setGetter(method);
            }
        }
    }

    private FieldInfo processField(final TypeElement parent, final VariableElement field) {
        final String name = TypeHelper.toFieldName(field.getSimpleName().toString());
        final TypeMirror type = field.asType();

        final FieldInfo fieldInfo = new FieldInfo(name, type);
        fieldInfo.setNullable(TypeHelper.isNullable(type));
        final FieldBasedObjectTransformer transformer = serializer.getFieldTransformer(fieldInfo).orElse(null);
        if (transformer == null) {
            logUnknownType(fieldInfo);
            return null;
        }

        fieldInfo.setTransformer(transformer);
        findAccessors(parent, fieldInfo);
        return fieldInfo;
    }

    private List<FieldInfo> gatherFields(final TypeElement element) {
        final List<FieldInfo> fields = new ArrayList<>();

        for (final Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.FIELD) continue;

            final VariableElement field = (VariableElement) enclosedElement;
            fields.add(processField(element, field));
        }

        return fields.stream()
                .filter(Objects::nonNull)
                .filter(field -> field.getGetter() != null && field.getSetter() != null)
                .toList();
    }

    private void generate(final TypeElement type, final RoundEnvironment roundEnv) {
        try {
            final List<FieldInfo> fields = gatherFields(type);
            final SerializationConfig config = new SerializationConfig();
            final SerializationContext context = new SerializationContext(
                    serializer,
                    config,
                    type,
                    fields,
                    roundEnv,
                    processingEnv
            );

            for (final Generator generator : generators) {
                generator.generate(context);
            }
        } catch (final IOException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(SerializableType.class.getCanonicalName());
    }
}
