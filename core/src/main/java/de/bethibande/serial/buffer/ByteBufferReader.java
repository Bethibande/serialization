package de.bethibande.serial.buffer;

import de.bethibande.serial.Reader;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferReader implements Reader {

    private ByteBuffer buffer;

    public ByteBufferReader() {
    }

    public ByteBufferReader(final ByteBuffer buffer) {
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
    public long available() {
        return buffer.remaining();
    }

    @Override
    public void read(final byte[] value) {
        buffer.get(value);
    }

    @Override
    public void read(final byte[] value, final int offset, final int length) {
        buffer.get(value, offset, length);
    }

    @Override
    public byte readByte() {
        return buffer.get();
    }

    @Override
    public short readShort() {
        return buffer.getShort();
    }

    @Override
    public int readInt() {
        return buffer.getInt();
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public float readFloat() {
        return buffer.getFloat();
    }

    @Override
    public double readDouble() {
        return buffer.getDouble();
    }

    @Override
    public boolean readBoolean() {
        return buffer.get() != 0;
    }

    @Override
    public char readChar() {
        return buffer.getChar();
    }

    @Override
    public String readString() {
        final byte[] bytes = new byte[readInt()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
