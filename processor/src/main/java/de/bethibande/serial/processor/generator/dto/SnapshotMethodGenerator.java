package de.bethibande.serial.processor.generator.dto;

import com.palantir.javapoet.*;
import de.bethibande.serial.HasSnapshots;
import de.bethibande.serial.processor.GenerationPostProcessor;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SnapshotMethodGenerator implements GenerationPostProcessor {

    private MethodSpec generateSnapshotMethod(final SerializationContext context) {
        final CodeBlock.Builder code = CodeBlock.builder();
        code.add("return new $T(\n", context.getSnapshotType());

        final List<FieldInfo> fields = context.fields();
        for (int i = 0; i < fields.size(); i++) {
            final FieldInfo field = fields.get(i);
            final boolean last = i == fields.size() - 1;

            if (!last) {
                code.add("  $N,\n", field.getFieldName());
            } else {
                code.add("  $N\n", field.getFieldName());
            }
        }
        code.add(");\n");

        return MethodSpec.methodBuilder("snapshot")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addCode(code.build())
                .returns(context.rawType())
                .build();
    }

    private MethodSpec generateRestoreMethod(final SerializationContext context) {
        final List<CodeBlock> parts = new ArrayList<>();
        for (final FieldInfo field : context.fields()) {
            parts.add(CodeBlock.builder()
                    .addStatement("this.$L = snapshot.$N()", field.getFieldName(), field.getGetterName())
                    .build());
        }
        final CodeBlock code = CodeBlock.join(parts, "");

        return MethodSpec.methodBuilder("restore")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(context.rawType(), "snapshot")
                .addCode(code)
                .build();
    }

    @Override
    public TypeSpec.Builder postProcess(final TypeSpec.Builder builder, final SerializationContext context) {
        if (!context.config().reduceBranching()) return builder;

        final TypeName type = ParameterizedTypeName.get(ClassName.get(HasSnapshots.class), context.rawType());

        return builder.addSuperinterface(type)
                .addMethod(generateSnapshotMethod(context))
                .addMethod(generateRestoreMethod(context));
    }
}
