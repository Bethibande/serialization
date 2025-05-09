package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
@SerializableType
public class BoxedIntegerType {
    private @NotNull Integer value = 0;

    public @NotNull Integer getValue() {
        return value;
    }

    public void setValue(final @NotNull Integer value) {
        this.value = value;
    }
}
