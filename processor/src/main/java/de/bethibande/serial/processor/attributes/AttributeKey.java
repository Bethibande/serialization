package de.bethibande.serial.processor.attributes;

import java.util.HashSet;
import java.util.Set;

public class AttributeKey<T> {

    private static final Set<String> KEYS = new HashSet<>();

    public static <T> AttributeKey<T> of(final String key) {
        if (KEYS.add(key)) return new AttributeKey<>(key);
        throw new IllegalArgumentException("Key already exists!");
    }

    private final String key;

    private AttributeKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
