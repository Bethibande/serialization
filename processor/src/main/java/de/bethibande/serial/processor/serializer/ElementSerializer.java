package de.bethibande.serial.processor.serializer;

import de.bethibande.serial.processor.generator.FieldInfo;

import java.util.List;
import java.util.Optional;

public class ElementSerializer {

    private final List<FieldBasedObjectTransformer> serializers;

    public ElementSerializer(final List<FieldBasedObjectTransformer> serializers) {
        this.serializers = serializers;
    }

    public Optional<FieldBasedObjectTransformer> getFieldTransformer(final FieldInfo field) {
        return serializers.stream()
                .filter(serializer -> serializer.isApplicable(field, this))
                .findFirst();
    }

}
