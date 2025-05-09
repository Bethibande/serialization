package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.EnumType;
import de.bethibande.serial.processor.test.dto.EnumTypeDeserializer;
import de.bethibande.serial.processor.test.dto.EnumTypeSerializer;
import org.junit.jupiter.api.Test;

public class EnumTypesTest extends SerializationTestBase {

    @Test
    public void testEnumType() {
        final EnumTypeSerializer serializer = new EnumTypeSerializer();
        final EnumTypeDeserializer deserializer = new EnumTypeDeserializer();
        final EnumType value = new EnumType();
        value.setValue(EnumType.TestEnum.VALUE_1);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
