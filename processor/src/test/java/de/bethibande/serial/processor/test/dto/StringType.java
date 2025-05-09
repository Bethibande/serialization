package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
@SerializableType
public class StringType {
    @NotNull
    private String value = "";

    public @NotNull String getValue() {
        return value;
    }

    public void setValue(final @NotNull String value) {
        this.value = value;
    }
}
