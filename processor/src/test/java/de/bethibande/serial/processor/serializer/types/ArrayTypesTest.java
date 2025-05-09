package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.IntArrayType;
import de.bethibande.serial.processor.test.dto.IntArrayTypeDeserializer;
import de.bethibande.serial.processor.test.dto.IntArrayTypeSerializer;
import org.junit.jupiter.api.Test;

public class ArrayTypesTest extends SerializationTestBase {

    @Test
    public void testEmptyIntArrayType() {
        final IntArrayTypeSerializer serializer = new IntArrayTypeSerializer();
        final IntArrayTypeDeserializer deserializer = new IntArrayTypeDeserializer();
        final IntArrayType value = new IntArrayType();
        value.setValue(new int[0]);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testIntArrayType() {
        final IntArrayTypeSerializer serializer = new IntArrayTypeSerializer();
        final IntArrayTypeDeserializer deserializer = new IntArrayTypeDeserializer();
        final IntArrayType value = new IntArrayType();
        value.setValue(new int[]{1, 2, 3});

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
