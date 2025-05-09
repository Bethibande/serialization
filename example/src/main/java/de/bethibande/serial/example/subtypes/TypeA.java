package de.bethibande.serial.example.subtypes;

import de.bethibande.serial.annotations.SerializableType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@SerializableType
public class TypeA {

    private TypeB value;

}
