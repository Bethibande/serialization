package de.bethibande.serial.example;

import de.bethibande.serial.annotations.ReduceBranching;
import de.bethibande.serial.annotations.SerializableType;

@SerializableType
@ReduceBranching
public interface ExampleType {

    int someInteger();

    long someLong();

}
