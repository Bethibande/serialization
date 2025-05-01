package de.bethibande.serial.processor;

import com.google.auto.service.AutoService;
import de.bethibande.serial.annotations.ReduceBranching;
import de.bethibande.serial.annotations.SerializableType;
import de.bethibande.serial.processor.context.SerializationConfig;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.dto.DTOGenerator;
import de.bethibande.serial.processor.generator.snapshot.SnapshotGenerator;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.TypeSerializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_23)
public class SerializationProcessor extends AbstractProcessor {

    private final List<Generator> generators = List.of(new DTOGenerator(), new SnapshotGenerator());
    private final ElementSerializer serializer;

    public SerializationProcessor() {
        final List<TypeSerializer> serializers = new ArrayList<>();
        ServiceLoader.load(TypeSerializer.class).forEach(serializers::add);
        ServiceLoader.load(TypeSerializer.class, SerializationProcessor.class.getClassLoader()).forEach(serializers::add);

        this.serializer = new ElementSerializer(serializers);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;

        final List<TypeElement> types = roundEnv.getElementsAnnotatedWith(SerializableType.class)
                .stream()
                .filter(it -> it.getKind().isInterface())
                .map(TypeElement.class::cast)
                .toList();

        for (final TypeElement type : types) {
            generate(type, roundEnv);
        }

        return false;
    }

    private boolean isKnownType(final ExecutableElement element) {
        return serializer.getSerializer(element).isPresent();
    }

    private List<FieldInfo> gatherFields(final TypeElement element) {
        final Map<String, FieldInfo> fields = new HashMap<>();

        for (final Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) continue;

            final ExecutableElement method = (ExecutableElement) enclosedElement;
            final String name = TypeHelper.getFieldName(method);
            final TypeMirror type = method.getReturnType();

            if (!isKnownType(method)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unknown type: " + type, method);
                continue;
            }

            final FieldInfo info = fields.computeIfAbsent(name, _ -> new FieldInfo(name, type));
            if (TypeHelper.isFluentSetter(method)) {
                info.setFluentSetter(method);
            } else if (TypeHelper.isSetter(method)) {
                info.setSetter(method);
            } else {
                info.setGetter(method);
            }
        }

        return new ArrayList<>(fields.values());
    }

    private void generate(final TypeElement type, final RoundEnvironment roundEnv) {
        try {
            final List<FieldInfo> fields = gatherFields(type);
            final boolean reduceBranching = type.getAnnotation(ReduceBranching.class) != null;
            final SerializationConfig config = new SerializationConfig(reduceBranching);
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
