package de.bethibande.serial.processor.serializer;

import javax.lang.model.type.TypeKind;

public final class Operations {

    public static final String WRITE_BYTE = "$L.writeByte($L)";
    public static final String WRITE_SHORT = "$L.writeShort($L)";
    public static final String WRITE_INT = "$L.writeInt($L)";
    public static final String WRITE_LONG = "$L.writeLong($L)";
    public static final String WRITE_FLOAT = "$L.writeFloat($L)";
    public static final String WRITE_DOUBLE = "$L.writeDouble($L)";
    public static final String WRITE_BOOLEAN = "$L.writeBoolean($L)";
    public static final String WRITE_CHAR = "$L.writeChar($L)";
    public static final String WRITE_STRING = "$L.writeString($L)";

    public static final String READ_BYTE = "return $L.readByte()";
    public static final String READ_SHORT = "return $L.readShort()";
    public static final String READ_INT = "return $L.readInt()";
    public static final String READ_LONG = "return $L.readLong()";
    public static final String READ_FLOAT = "return $L.readFloat()";
    public static final String READ_DOUBLE = "return $L.readDouble()";
    public static final String READ_BOOLEAN = "return $L.readBoolean()";
    public static final String READ_CHAR = "return $L.readChar()";
    public static final String READ_STRING = "return $L.readString()";

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

    private Operations() {}

}
