package de.bethibande.serial.processor.test.dto;

import de.bethibande.serial.annotations.SerializableType;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ToString
@EqualsAndHashCode
@SerializableType
public class FloatListType {
    private @NotNull List<Float> value = List.of();

    public @NotNull List<Float> getValue() {
        return value;
    }

    public void setValue(final @NotNull List<Float> value) {
        this.value = value;
    }
}
