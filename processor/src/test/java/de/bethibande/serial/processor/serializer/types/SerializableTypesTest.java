package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.StringType;
import de.bethibande.serial.processor.test.dto.SubTypeType;
import de.bethibande.serial.processor.test.dto.SubTypeTypeDeserializer;
import de.bethibande.serial.processor.test.dto.SubTypeTypeSerializer;
import org.junit.jupiter.api.Test;

public class SerializableTypesTest extends SerializationTestBase {

    @Test
    public void testSubTypeType() {
        final SubTypeTypeSerializer serializer = new SubTypeTypeSerializer();
        final SubTypeTypeDeserializer deserializer = new SubTypeTypeDeserializer();
        final SubTypeType value = new SubTypeType();
        final StringType subValue = new StringType();
        subValue.setValue("Hello World!");
        value.setValue(subValue);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
