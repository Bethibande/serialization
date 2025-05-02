package de.bethibande.serial.processor.serializer;

import de.bethibande.serial.processor.generator.FieldInfo;

import java.util.HashMap;

public abstract class EmbeddedTypeTransformer implements FieldBasedObjectTransformer {

    protected abstract boolean isApplicable0(final FieldInfo field);

    protected abstract FieldInfo createChildType(final FieldInfo field, final ElementSerializer serializer);

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        if (!isApplicable0(field)) return false;

        final FieldInfo child = createChildType(field, serializer)
                .toBuilder()
                .generatedMethods(new HashMap<>()) // Avoid sharing the same map instance with the parent
                .build();
        child.setTransformer(serializer.getFieldTransformer(child).orElseThrow());
        child.setParent(field);
        field.setChild(child);

        return true;
    }

    public String embeddedMethodName(final FieldInfo field) {
        return methodName(field.getChild());
    }

}
