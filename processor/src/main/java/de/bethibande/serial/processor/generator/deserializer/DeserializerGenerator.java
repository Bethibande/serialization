package de.bethibande.serial.processor.generator.deserializer;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import de.bethibande.serial.impl.AbstractDeserializer;
import de.bethibande.serial.processor.Generator;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.AbstractGenerator;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(Generator.class)
public class DeserializerGenerator extends AbstractGenerator {

    public static final ClassName ABSTRACT_DESERIALIZER = ClassName.get(AbstractDeserializer.class);

    public DeserializerGenerator() {
        withPostProcessor(new ReadMethodProcessor());
    }

    @Override
    public ClassName typeName(final SerializationContext context) {
        return context.deserializerType();
    }

    @Override
    public TypeSpec.Builder generateType(final SerializationContext context) {
        final TypeName superType = ParameterizedTypeName.get(ABSTRACT_DESERIALIZER, context.rawType());

        final TypeSpec.Builder builder = TypeSpec.classBuilder(context.deserializerType())
                .addModifiers(Modifier.PUBLIC)
                .addInitializerBlock(CodeBlock.builder()
                        .addStatement("super.allocator = () -> new $T()", context.type())
                        .build())
                .superclass(superType);

        for (final FieldInfo field : context.fields()) {
            field.getTransformer()
                    .transformType(builder, field, MethodType.DESERIALIZE, context);
        }

        return builder;
    }
}
