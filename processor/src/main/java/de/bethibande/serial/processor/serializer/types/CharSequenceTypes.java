package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.MethodSpec;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.AbstractSingleMethodGenerator;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import de.bethibande.serial.processor.serializer.Operations;

@AutoService(FieldBasedObjectTransformer.class)
public class CharSequenceTypes extends AbstractSingleMethodGenerator {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && TypeHelper.isCharSequence(field.getType());
    }

    @Override
    protected MethodSpec generateSerializerMethod(final FieldInfo field, final SerializationContext ctx) {
        return defaultWriteMethod(field, ctx)
                .addStatement(Operations.WRITE_STRING, "target", field.getFieldName())
                .addStatement("return this")
                .build();
    }

    @Override
    protected MethodSpec generateDeserializerMethod(final FieldInfo field, final SerializationContext ctx) {
        return defaultReadMethod(field, ctx)
                .addStatement(Operations.READ_STRING, "reader")
                .build();
    }
}
