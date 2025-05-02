package de.bethibande.serial.processor.serializer;

import javax.lang.model.type.TypeKind;

public class Operations {

    public static String WRITE_BYTE = "$L.writeByte($L)";
    public static String WRITE_SHORT = "$L.writeShort($L)";
    public static String WRITE_INT = "$L.writeInt($L)";
    public static String WRITE_LONG = "$L.writeLong($L)";
    public static String WRITE_FLOAT = "$L.writeFloat($L)";
    public static String WRITE_DOUBLE = "$L.writeDouble($L)";
    public static String WRITE_BOOLEAN = "$L.writeBoolean($L)";
    public static String WRITE_CHAR = "$L.writeChar($L)";
    public static String WRITE_STRING = "$L.writeString($L)";

    public static String READ_BYTE = "return $L.readByte()";
    public static String READ_SHORT = "return $L.readShort()";
    public static String READ_INT = "return $L.readInt()";
    public static String READ_LONG = "return $L.readLong()";
    public static String READ_FLOAT = "return $L.readFloat()";
    public static String READ_DOUBLE = "return $L.readDouble()";
    public static String READ_BOOLEAN = "return $L.readBoolean()";
    public static String READ_CHAR = "return $L.readChar()";
    public static String READ_STRING = "return $L.readString()";

    public static String writeOperation(final TypeKind kind) {
        return switch (kind) {
            case BYTE -> Operations.WRITE_BYTE;
            case SHORT -> Operations.WRITE_SHORT;
            case INT -> Operations.WRITE_INT;
            case LONG -> Operations.WRITE_LONG;
            case FLOAT -> Operations.WRITE_FLOAT;
            case DOUBLE -> Operations.WRITE_DOUBLE;
            case BOOLEAN -> Operations.WRITE_BOOLEAN;
            case CHAR -> Operations.WRITE_CHAR;
            default -> throw new IllegalArgumentException("Unknown primitive type: " + kind);
        };
    }

    public static String readOperation(final TypeKind kind) {
        return switch (kind) {
            case BYTE -> Operations.READ_BYTE;
            case SHORT -> Operations.READ_SHORT;
            case INT -> Operations.READ_INT;
            case LONG -> Operations.READ_LONG;
            case FLOAT -> Operations.READ_FLOAT;
            case DOUBLE -> Operations.READ_DOUBLE;
            case BOOLEAN -> Operations.READ_BOOLEAN;
            case CHAR -> Operations.READ_CHAR;
            default -> throw new IllegalArgumentException("Unknown primitive type: " + kind);
        };
    }

}
