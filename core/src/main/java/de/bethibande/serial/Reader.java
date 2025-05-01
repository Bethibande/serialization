package de.bethibande.serial;

public interface Reader {

    long position();

    long available();

    void read(final byte[] value);

    void read(final byte[] value, final int offset, final int length);

    byte readByte();

    short readShort();

    int readInt();

    long readLong();

    float readFloat();

    double readDouble();

    boolean readBoolean();

    char readChar();

    String readString();

}
