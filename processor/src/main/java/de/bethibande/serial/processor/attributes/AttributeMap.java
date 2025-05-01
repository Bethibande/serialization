package de.bethibande.serial.processor.attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AttributeMap implements HasAttributes {

    private final Map<String, Object> map = new HashMap<>();

    @Override
    public <T> void set(final AttributeKey<T> key, final T value) {
        map.put(key.getKey(), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(final AttributeKey<T> key) {
        return Optional.ofNullable((T) map.get(key.getKey()));
    }
}
