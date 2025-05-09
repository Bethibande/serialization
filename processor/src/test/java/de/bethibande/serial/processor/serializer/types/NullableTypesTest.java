package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.NullableStringType;
import de.bethibande.serial.processor.test.dto.NullableStringTypeDeserializer;
import de.bethibande.serial.processor.test.dto.NullableStringTypeSerializer;
import org.junit.jupiter.api.Test;

public class NullableTypesTest extends SerializationTestBase {

    @Test
    public void testNullValue() {
        final NullableStringTypeSerializer serializer = new NullableStringTypeSerializer();
        final NullableStringTypeDeserializer deserializer = new NullableStringTypeDeserializer();
        final NullableStringType value = new NullableStringType();
        value.setValue(null);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testNonNullValue() {
        final NullableStringTypeSerializer serializer = new NullableStringTypeSerializer();
        final NullableStringTypeDeserializer deserializer = new NullableStringTypeDeserializer();
        final NullableStringType value = new NullableStringType();
        value.setValue("Hello World!");

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
