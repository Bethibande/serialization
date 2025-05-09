package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class ByteType {
    private byte value;

    public void setValue(final byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

