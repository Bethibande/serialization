package de.bethibande.serial.netty;

import de.bethibande.serial.Reader;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class NettyReader extends AbstractNettySerial implements Reader {

    public NettyReader() {
    }

    public NettyReader(final ByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void read(final byte[] value) {
        buffer.readBytes(value);
    }

    @Override
    public void read(final byte[] value, final int offset, final int length) {
        buffer.readBytes(value, offset, length);
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public short readShort() {
        return buffer.readShort();
    }

    @Override
    public int readInt() {
        return buffer.readInt();
    }

    @Override
    public long readLong() {
        return buffer.readLong();
    }

    @Override
    public float readFloat() {
        return buffer.readFloat();
    }

    @Override
    public double readDouble() {
        return buffer.readDouble();
    }

    @Override
    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    @Override
    public char readChar() {
        return buffer.readChar();
    }

    @Override
    public String readString() {
        final int length = buffer.readInt();
        return buffer.readCharSequence(length, StandardCharsets.UTF_8).toString();
    }
}
