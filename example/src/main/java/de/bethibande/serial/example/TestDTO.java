package de.bethibande.serial.example;

import de.bethibande.serial.annotations.SerializableType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
@SerializableType
public class TestDTO {

    private int someNumber;
    private boolean someBoolean;
    private @NotNull String someString;
    private String someOtherString;

    public TestDTO withSomeNumber(final int number) {
        this.someNumber = number;
        return this;
    }

    public TestDTO withSomeString(final String string) {
        this.someString = string;
        return this;
    }

    public TestDTO withSomeOtherString(final String string) {
        this.someOtherString = string;
        return this;
    }

}
