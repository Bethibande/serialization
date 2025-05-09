package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@SerializableType
public class EnumType {

    public enum TestEnum {
        VALUE_1, VALUE_2, Value_3
    }

    private TestEnum value;

    public TestEnum getValue() {
        return value;
    }

    public void setValue(final TestEnum value) {
        this.value = value;
    }

}
