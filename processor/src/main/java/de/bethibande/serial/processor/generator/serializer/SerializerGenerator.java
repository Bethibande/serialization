package de.bethibande.serial.processor.generator.serializer;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.impl.AbstractSerializer;
import de.bethibande.serial.processor.Generator;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.AbstractGenerator;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(Generator.class)
public class SerializerGenerator extends AbstractGenerator {

    public static final ClassName ABSTRACT_SERIALIZER = ClassName.get(AbstractSerializer.class);

    public SerializerGenerator() {
        withPostProcessor(new WriteMethodProcessor());
    }

    @Override
    public ClassName typeName(final SerializationContext context) {
        return context.serializerType();
    }

    @Override
    public TypeSpec.Builder generateType(final SerializationContext context) {
        final TypeName superType = ParameterizedTypeName.get(ABSTRACT_SERIALIZER, context.rawType());

        final TypeSpec.Builder builder = TypeSpec.classBuilder(context.serializerType())
                .superclass(superType);

        final Map<FieldBasedObjectTransformer, List<FieldInfo>> transformers = context.fields()
                .stream()
                .collect(Collectors.groupingBy(FieldInfo::getTransformer));

        transformers.forEach((transformer, fields) -> transformer.transformSerializer(builder, fields, context));

        return builder;
    }
}
