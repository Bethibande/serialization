package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class SubTypeType {
    private StringType value;

    public StringType getValue() {
        return value;
    }

    public void setValue(final StringType value) {
        this.value = value;
    }
}
