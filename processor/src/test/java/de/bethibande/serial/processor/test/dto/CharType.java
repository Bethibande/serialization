package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class CharType {
    private char value;

    public char getValue() {
        return value;
    }

    public void setValue(final char value) {
        this.value = value;
    }
}
