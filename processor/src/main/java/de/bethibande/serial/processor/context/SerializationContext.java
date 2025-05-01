package de.bethibande.serial.processor.context;

import com.palantir.javapoet.ClassName;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

public record SerializationContext(
        ElementSerializer serializer,
        SerializationConfig config,
        TypeElement type,
        List<FieldInfo> fields,
        RoundEnvironment roundEnv,
        ProcessingEnvironment processingEnv
) {

    public ClassName rawType() {
        return ClassName.get(type);
    }

    public ClassName generatedType() {
        return TypeHelper.getDTOName(rawType());
    }

    public ClassName getSnapshotType() {
        return TypeHelper.getSnapshotName(rawType());
    }

}
