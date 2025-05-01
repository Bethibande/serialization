package de.bethibande.serial.netty;

import de.bethibande.serial.Writer;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class NettyWriter extends AbstractNettySerial implements Writer {

    private int markStart;

    public NettyWriter() {
    }

    public NettyWriter(final ByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void markStart() {
        this.markStart = buffer.writerIndex();
        buffer.skipBytes(4);
    }

    @Override
    public void markEnd() {
        final int length = buffer.writerIndex() - this.markStart - 4;
        buffer.setInt(this.markStart, length);
    }

    @Override
    public void write(final byte[] value) {
        buffer.writeBytes(value);
    }

    @Override
    public void write(final byte[] value, final int offset, final int length) {
        buffer.writeBytes(value, offset, length);
    }

    @Override
    public void writeByte(final byte value) {
        buffer.writeByte(value);
    }

    @Override
    public void writeShort(final short value) {
        buffer.writeShort(value);
    }

    @Override
    public void writeInt(final int value) {
        buffer.writeInt(value);
    }

    @Override
    public void writeLong(final long value) {
        buffer.writeLong(value);
    }

    @Override
    public void writeFloat(final float value) {
        buffer.writeFloat(value);
    }

    @Override
    public void writeDouble(final double value) {
        buffer.writeDouble(value);
    }

    @Override
    public void writeBoolean(final boolean value) {
        buffer.writeBoolean(value);
    }

    @Override
    public void writeChar(final char value) {
        buffer.writeChar(value);
    }

    @Override
    public void writeString(final String value) {
        final int index = buffer.writerIndex();
        buffer.skipBytes(4);

        final int written = buffer.writeCharSequence(value, StandardCharsets.UTF_8);
        buffer.setInt(index, written);
    }
}
