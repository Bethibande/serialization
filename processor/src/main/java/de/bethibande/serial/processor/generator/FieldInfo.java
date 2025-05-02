package de.bethibande.serial.processor.generator;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.attributes.AttributeKey;
import de.bethibande.serial.processor.attributes.AttributeMap;
import de.bethibande.serial.processor.attributes.HasAttributes;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;
import lombok.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldInfo implements HasAttributes {

    @Getter
    private final TypeElement parent;
    @Getter
    private final VariableElement field;
    @Getter
    private final String fieldName;
    @Getter
    private final TypeMirror type;

    private AttributeMap attributes;

    @Getter
    @Setter
    private ExecutableElement getter;
    @Getter
    @Setter
    private ExecutableElement setter;
    @Getter
    @Setter
    private ExecutableElement fluentSetter;

    @Getter
    @Setter
    private boolean nullable;

    @Getter
    @Setter
    private FieldBasedObjectTransformer transformer;

    private Map<MethodType, MethodSpec> generatedMethods;

    public FieldInfo(final TypeElement parent, final VariableElement field, final String fieldName, final TypeMirror type) {
        this.parent = parent;
        this.field = field;
        this.fieldName = fieldName;
        this.type = type;
        this.attributes = new AttributeMap();
        this.nullable = false;
        this.generatedMethods = new HashMap<>();
    }

    public TypeElement getTypeElement() {
        return TypeHelper.asElement(type);
    }

    public TypeName getTypeName() {
        return TypeName.get(type);
    }

    public String getGetterName() {
        return getter.getSimpleName().toString();
    }

    public boolean usesShortNames() {
        return !getGetterName().startsWith("get") && !getGetterName().startsWith("is");
    }

    public String getSetterName() {
        if (setter != null) return setter.getSimpleName().toString();

        if (!usesShortNames()) {
            return "set" + TypeHelper.capitalizeName(fieldName);
        } else {
            return fieldName;
        }
    }

    public String getFluentSetterName() {
        if (fluentSetter != null) return fluentSetter.getSimpleName().toString();
        // No short-name here since it's signature would collide with the setter
        return "with" + TypeHelper.capitalizeName(fieldName);
    }

    public MethodSpec getGeneratedMethod(final MethodType type) {
        return generatedMethods.get(type);
    }

    public void addGeneratedMethod(final MethodType type, final MethodSpec method) {
        this.generatedMethods.put(type, method);
    }

    @Override
    public <T> void set(final AttributeKey<T> key, final T value) {
        attributes.set(key, value);
    }

    @Override
    public <T> Optional<T> get(final AttributeKey<T> key) {
        return attributes.get(key);
    }

    @Override
    public String toString() {
        return type + " " + fieldName;
    }
}
