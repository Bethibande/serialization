package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.generator.MethodType;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public abstract class AbstractSingleMethodGenerator implements FieldBasedObjectTransformer {

    protected MethodSpec.Builder defaultWriteMethod(final FieldInfo field, final SerializationContext ctx) {
        return MethodSpec.methodBuilder(field.getFieldName())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(field.getTypeName(), field.getFieldName())
                        .addModifiers(Modifier.FINAL)
                        .build())
                .returns(ctx.serializerType());
    }

    protected MethodSpec.Builder defaultReadMethod(final FieldInfo field, final SerializationContext ctx) {
        return MethodSpec.methodBuilder(field.getFieldName())
                .addModifiers(Modifier.PUBLIC)
                .returns(field.getTypeName());
    }

    protected void generateMethods(final TypeSpec.Builder builder,
                                   final Collection<FieldInfo> fields,
                                   final SerializationContext ctx,
                                   final BiFunction<FieldInfo, SerializationContext, MethodSpec> generator,
                                   final MethodType type) {
        final List<MethodSpec> methods = new ArrayList<>();
        for (final FieldInfo field : fields) {
            final MethodSpec method = generator.apply(field, ctx);

            field.addGeneratedMethod(type, method);
            methods.add(method);
        }

        builder.addMethods(methods);
    }

    @Override
    public void transformSerializer(final TypeSpec.Builder builder,
                                    final Collection<FieldInfo> fields,
                                    final SerializationContext ctx) {
        generateMethods(builder, fields, ctx, this::generateSerializerMethod, MethodType.WRITE);
    }

    protected abstract MethodSpec generateSerializerMethod(final FieldInfo field, final SerializationContext ctx);

    @Override
    public void transformDeserializer(final TypeSpec.Builder builder, final Collection<FieldInfo> fields, final SerializationContext ctx) {
        generateMethods(builder, fields, ctx, this::generateDeserializerMethod, MethodType.READ);
    }

    protected abstract MethodSpec generateDeserializerMethod(final FieldInfo field, final SerializationContext ctx);
}
