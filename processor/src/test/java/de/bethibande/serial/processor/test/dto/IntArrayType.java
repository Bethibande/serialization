package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class IntArrayType {
    private int[] value;

    public int[] getValue() {
        return value;
    }

    public void setValue(final int[] value) {
        this.value = value;
    }
}
