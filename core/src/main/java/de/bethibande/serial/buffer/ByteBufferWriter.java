package de.bethibande.serial.buffer;

import de.bethibande.serial.Writer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferWriter implements Writer {

    private ByteBuffer buffer;

    public ByteBufferWriter() {
    }

    public ByteBufferWriter(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public long position() {
        return buffer.position();
    }

    @Override
    public void skip(final int bytes) {
        buffer.position(buffer.position() + bytes);
    }

    @Override
    public void write(final byte[] value) {
        buffer.put(value);
    }

    @Override
    public void write(final byte[] value, final int offset, final int length) {
        buffer.put(value, offset, length);
    }

    @Override
    public void writeByte(final byte value) {
        buffer.put(value);
    }

    @Override
    public void writeShort(final short value) {
        buffer.putShort(value);
    }

    @Override
    public void writeInt(final int value) {
        buffer.putInt(value);
    }

    @Override
    public void writeLong(final long value) {
        buffer.putLong(value);
    }

    @Override
    public void writeFloat(final float value) {
        buffer.putFloat(value);
    }

    @Override
    public void writeDouble(final double value) {
        buffer.putDouble(value);
    }

    @Override
    public void writeBoolean(final boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }

    @Override
    public void writeChar(final char value) {
        buffer.putChar(value);
    }

    @Override
    public void writeString(final String value) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
    }
}
