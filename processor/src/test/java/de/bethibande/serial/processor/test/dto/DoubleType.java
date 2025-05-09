package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class DoubleType {
    private double value;

    public void setValue(final double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
