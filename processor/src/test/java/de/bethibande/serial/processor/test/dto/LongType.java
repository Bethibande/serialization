package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class LongType {
    private long value;

    public void setValue(final long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

}

