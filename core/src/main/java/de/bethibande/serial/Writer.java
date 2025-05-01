package de.bethibande.serial;

public interface Writer {

    /**
     * Sets a marker at the beginning of the current write operation and reserves four bytes for the length signature.
     */
    void markStart();

    /**
     * Calculates the number of written bytes since the last call to {@link #markStart()},
     * not including the 4 reserved bytes, and writes it at the position recorded by {@link #markStart()}.
     */
    void markEnd();

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
