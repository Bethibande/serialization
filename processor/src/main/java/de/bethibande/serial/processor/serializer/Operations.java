package de.bethibande.serial.processor.serializer;

public class Operations {

    public static String WRITE_BYTE = "writer.writeByte($L)";
    public static String WRITE_SHORT = "writer.writeShort($L)";
    public static String WRITE_INT = "writer.writeInt($L)";
    public static String WRITE_LONG = "writer.writeLong($L)";
    public static String WRITE_FLOAT = "writer.writeFloat($L)";
    public static String WRITE_DOUBLE = "writer.writeDouble($L)";
    public static String WRITE_BOOLEAN = "writer.writeBoolean($L)";
    public static String WRITE_CHAR = "writer.writeChar($L)";
    public static String WRITE_STRING = "writer.writeString($S)";

    public static String READ_BYTE = "final $T $L = reader.readByte()";
    public static String READ_SHORT = "final $T $L = reader.readShort()";
    public static String READ_INT = "final $T $L = reader.readInt()";
    public static String READ_LONG = "final $T $L = reader.readLong()";
    public static String READ_FLOAT = "final $T $L = reader.readFloat()";
    public static String READ_DOUBLE = "final $T $L = reader.readDouble()";
    public static String READ_BOOLEAN = "final $T $L = reader.readBoolean()";
    public static String READ_CHAR = "final $T $L = reader.readChar()";
    public static String READ_STRING = "final $T $L = reader.readString()";

}
