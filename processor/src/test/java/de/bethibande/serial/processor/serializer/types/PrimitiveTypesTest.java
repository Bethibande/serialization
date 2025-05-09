package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.*;
import org.junit.jupiter.api.Test;

public class PrimitiveTypesTest extends SerializationTestBase {

    @Test
    public void testByteType() {
        final ByteTypeSerializer serializer = new ByteTypeSerializer();
        final ByteTypeDeserializer deserializer = new ByteTypeDeserializer();
        final ByteType value = new ByteType();
        value.setValue((byte) 123);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testShortType() {
        final ShortTypeSerializer serializer = new ShortTypeSerializer();
        final ShortTypeDeserializer deserializer = new ShortTypeDeserializer();
        final ShortType value = new ShortType();
        value.setValue((short) 123);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testIntegerType() {
        final IntegerTypeSerializer serializer = new IntegerTypeSerializer();
        final IntegerTypeDeserializer deserializer = new IntegerTypeDeserializer();
        final IntegerType value = new IntegerType();
        value.setValue(123);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testLongType() {
        final LongTypeSerializer serializer = new LongTypeSerializer();
        final LongTypeDeserializer deserializer = new LongTypeDeserializer();
        final LongType value = new LongType();
        value.setValue(123L);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testFloatType() {
        final FloatTypeSerializer serializer = new FloatTypeSerializer();
        final FloatTypeDeserializer deserializer = new FloatTypeDeserializer();
        final FloatType value = new FloatType();
        value.setValue(123.456f);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testDoubleType() {
        final DoubleTypeSerializer serializer = new DoubleTypeSerializer();
        final DoubleTypeDeserializer deserializer = new DoubleTypeDeserializer();
        final DoubleType value = new DoubleType();
        value.setValue(123.456);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testBooleanType() {
        final BooleanTypeSerializer serializer = new BooleanTypeSerializer();
        final BooleanTypeDeserializer deserializer = new BooleanTypeDeserializer();
        final BooleanType value = new BooleanType();
        value.setValue(true);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testCharType() {
        final CharTypeSerializer serializer = new CharTypeSerializer();
        final CharTypeDeserializer deserializer = new CharTypeDeserializer();
        final CharType value = new CharType();
        value.setValue('a');

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }
}
