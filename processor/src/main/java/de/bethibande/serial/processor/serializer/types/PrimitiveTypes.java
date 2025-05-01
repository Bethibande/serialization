package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import de.bethibande.serial.processor.serializer.Operations;

import javax.lang.model.type.TypeKind;

@AutoService(FieldBasedObjectTransformer.class)
public class PrimitiveTypes implements FieldBasedObjectTransformer {

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        return field.getType()
                .getKind()
                .isPrimitive();
    }

    @Override
    public CodeBlock createSerializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(writeOperation(field.getType().getKind()), "target", field.getFieldName())
                .addStatement("return this")
                .build();
    }

    @Override
    public CodeBlock createDeserializerCode(final FieldInfo field, final SerializationContext ctx) {
        return CodeBlock.builder()
                .addStatement(readOperation(field.getType().getKind()), "reader")
                .build();
    }

    private int size(final TypeKind kind) {
        return switch (kind) {
            case BYTE, BOOLEAN -> 1;
            case SHORT, CHAR -> 2;
            case INT, FLOAT -> 4;
            case LONG, DOUBLE -> 8;
            default -> throw new IllegalArgumentException("Unknown primitive type: " + kind);
        };
    }

    private String writeOperation(final TypeKind kind) {
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

    private String readOperation(final TypeKind kind) {
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
