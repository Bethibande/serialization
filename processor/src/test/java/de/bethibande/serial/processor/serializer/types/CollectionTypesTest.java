package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.FloatListType;
import de.bethibande.serial.processor.test.dto.FloatListTypeDeserializer;
import de.bethibande.serial.processor.test.dto.FloatListTypeSerializer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionTypesTest extends SerializationTestBase {

    @Test
    public void testEmptyFloatListType() {
        final FloatListTypeSerializer serializer = new FloatListTypeSerializer();
        final FloatListTypeDeserializer deserializer = new FloatListTypeDeserializer();
        deserializer.valueAllocator(ArrayList::new);
        final FloatListType value = new FloatListType();
        value.setValue(List.of());

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

    @Test
    public void testFloatListType() {
        final FloatListTypeSerializer serializer = new FloatListTypeSerializer();
        final FloatListTypeDeserializer deserializer = new FloatListTypeDeserializer();
        deserializer.valueAllocator(ArrayList::new);
        final FloatListType value = new FloatListType();
        value.setValue(List.of(1.23f, 4.56f));

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
