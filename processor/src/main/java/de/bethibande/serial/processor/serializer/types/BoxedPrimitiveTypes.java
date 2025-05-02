package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.type.TypeKind;

@AutoService(FieldBasedObjectTransformer.class)
public class BoxedPrimitiveTypes extends PrimitiveTypes {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return !field.isNullable() && TypeHelper.isBoxedPrimitive(field.getType());
    }

    @Override
    protected TypeKind toTypeKind(final FieldInfo field) {
        return TypeHelper.unbox(field.getType()).getKind();
    }
}
