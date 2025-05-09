package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class ShortType {
    private short value;

    public void setValue(final short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}

