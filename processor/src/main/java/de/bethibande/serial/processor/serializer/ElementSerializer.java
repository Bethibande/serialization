package de.bethibande.serial.processor.serializer;

import javax.lang.model.AnnotatedConstruct;
import java.util.List;
import java.util.Optional;

public class ElementSerializer {

    private final List<TypeSerializer> serializers;

    public ElementSerializer(final List<TypeSerializer> serializers) {
        this.serializers = serializers;
    }

    public Optional<TypeSerializer> getSerializer(final AnnotatedConstruct element) {
        return serializers.stream()
                .filter(serializer -> serializer.isApplicable(element, this))
                .findFirst();
    }

}
