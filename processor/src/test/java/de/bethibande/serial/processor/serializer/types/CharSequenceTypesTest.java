package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.StringType;
import de.bethibande.serial.processor.test.dto.StringTypeDeserializer;
import de.bethibande.serial.processor.test.dto.StringTypeSerializer;
import org.junit.jupiter.api.Test;

public class CharSequenceTypesTest extends SerializationTestBase {


    @Test
    public void testStringType() {
        final StringTypeSerializer serializer = new StringTypeSerializer();
        final StringTypeDeserializer deserializer = new StringTypeDeserializer();
        final StringType value = new StringType();
        value.setValue("Hello World!");

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
