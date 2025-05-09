package de.bethibande.serial.processor;

import de.bethibande.serial.Deserializer;
import de.bethibande.serial.Reader;
import de.bethibande.serial.Serializer;
import de.bethibande.serial.Writer;
import de.bethibande.serial.buffer.ByteBufferReader;
import de.bethibande.serial.buffer.ByteBufferWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTestBase {

    protected ByteBuffer buffer;
    protected Writer writer;
    protected Reader reader;

    @BeforeEach
    public void setup() {
        this.buffer = ByteBuffer.allocate(1024);
        this.writer = new ByteBufferWriter(this.buffer);
        this.reader = new ByteBufferReader(this.buffer);
    }

    @AfterEach
    public void teardown() {
        this.buffer = null;
    }

    protected <T> void testSerializationAndDeserialization(final Serializer<T> serializer,
                                                           final Deserializer<T> deserializer,
                                                           final T value) {
        serializer.bind(this.writer).write(value);
        this.buffer.flip();

        final T output = deserializer.bind(this.reader).read();
        assertEquals(value, output);
    }

}
