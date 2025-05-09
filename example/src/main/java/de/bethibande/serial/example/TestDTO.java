package de.bethibande.serial.example;

import de.bethibande.serial.annotations.SerializableType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@SerializableType
public class TestDTO {

    private int someNumber;
    private boolean someBoolean;
    private @NotNull String someString;
    private String someOtherString;
    @SuppressWarnings("boxedtypes")
    private Integer someInteger;
    private MyEnum someEnum;
    private @NotNull String[] someStringArray;

    private List<String> stringList;

    // We need to prevent the default lombok setter here, since lombok handles annotations incorrectly adding a null-check that shouldn't be there.
    public void setSomeStringArray(final String[] someStringArray) {
        this.someStringArray = someStringArray;
    }

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
