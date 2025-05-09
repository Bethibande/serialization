package de.bethibande.serial.processor.serializer.types;

import de.bethibande.serial.processor.SerializationTestBase;
import de.bethibande.serial.processor.test.dto.BoxedIntegerType;
import de.bethibande.serial.processor.test.dto.BoxedIntegerTypeDeserializer;
import de.bethibande.serial.processor.test.dto.BoxedIntegerTypeSerializer;
import org.junit.jupiter.api.Test;

public class BoxedPrimitiveTypesTest extends SerializationTestBase {

    @Test
    public void testBoxedIntegerType() {
        final BoxedIntegerTypeSerializer serializer = new BoxedIntegerTypeSerializer();
        final BoxedIntegerTypeDeserializer deserializer = new BoxedIntegerTypeDeserializer();
        final BoxedIntegerType value = new BoxedIntegerType();
        value.setValue(123);

        this.testSerializationAndDeserialization(serializer, deserializer, value);
    }

}
