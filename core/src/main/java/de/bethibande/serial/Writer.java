package de.bethibande.serial;

public interface Writer {

    long position();

    void skip(final int bytes);

    void write(final byte[] value);

    void write(final byte[] value, final int offset, final int length);

    void writeByte(final byte value);

    void writeShort(final short value);

    void writeInt(final int value);

    void writeLong(final long value);

    void writeFloat(final float value);

    void writeDouble(final double value);

    void writeBoolean(final boolean value);

    void writeChar(final char value);

    void writeString(final String value);

}
