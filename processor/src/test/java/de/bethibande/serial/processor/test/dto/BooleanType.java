package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class BooleanType {
    private boolean value;

    public void setValue(final boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }
}
