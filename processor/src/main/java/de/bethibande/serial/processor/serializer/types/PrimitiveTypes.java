package de.bethibande.serial.processor.serializer.types;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.CodeBlock;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.serializer.ElementSerializer;
import de.bethibande.serial.processor.serializer.Operations;
import de.bethibande.serial.processor.serializer.SizeCalculator;
import de.bethibande.serial.processor.serializer.TypeSerializer;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@AutoService(TypeSerializer.class)
public class PrimitiveTypes implements TypeSerializer {

    @Override
    public boolean isApplicable(final AnnotatedConstruct construct, final ElementSerializer serializer) {
        return asTypeMirror(construct)
                .getKind()
                .isPrimitive();
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

    @Override
    public SizeCalculator size(final AnnotatedConstruct construct, final ElementSerializer serializer) {
        final TypeMirror typeMirror = asTypeMirror(construct);
        final int size = size(typeMirror.getKind());

        return SizeCalculator.ofStaticSize(size);
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

    @Override
    public CodeBlock write(final AnnotatedConstruct construct, final ElementSerializer serializer) {
        final Element element = asElement(construct);
        final TypeMirror typeMirror = asTypeMirror(construct);
        final String operation = writeOperation(typeMirror.getKind());

        return CodeBlock.builder()
                .addStatement(operation, TypeHelper.getFieldName(element))
                .build();
    }

    @Override
    public CodeBlock read(final AnnotatedConstruct construct, final ElementSerializer serializer) {
        final Element element = asElement(construct);
        final TypeMirror typeMirror = asTypeMirror(construct);
        final String operation = readOperation(typeMirror.getKind());
        final String fieldName = TypeHelper.getFieldName(element);

        return CodeBlock.builder()
                .addStatement(operation, typeMirror, fieldName)
                .addStatement("this.$L = $L", fieldName, fieldName)
                .build();
    }
}
