package de.bethibande.serial.processor.generator;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.attributes.AttributeKey;
import de.bethibande.serial.processor.attributes.AttributeMap;
import de.bethibande.serial.processor.attributes.HasAttributes;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FieldInfo implements HasAttributes {

    private final TypeElement parent;
    private final VariableElement field;
    private final String fieldName;
    private final TypeMirror type;
    private final TypeElement typeElement;

    private final AttributeMap attributes = new AttributeMap();

    private ExecutableElement getter;
    private ExecutableElement setter;
    private ExecutableElement fluentSetter;

    private boolean nullable = false;

    private FieldBasedObjectTransformer transformer;
    private Map<MethodType, MethodSpec> generatedMethods = new HashMap<>();

    public FieldInfo(final TypeElement parent, final VariableElement field, final String fieldName, final TypeMirror type) {
        this.parent = parent;
        this.field = field;
        this.fieldName = fieldName;
        this.type = type;
        this.typeElement = TypeHelper.asElement(type);
    }


    public TypeElement getParent() {
        return parent;
    }

    public VariableElement getField() {
        return field;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getFieldName() {
        return fieldName;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeName getTypeName() {
        return TypeName.get(type);
    }

    public ExecutableElement getGetter() {
        return getter;
    }

    public ExecutableElement getSetter() {
        return setter;
    }

    public ExecutableElement getFluentSetter() {
        return fluentSetter;
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

    public FieldBasedObjectTransformer getTransformer() {
        return transformer;
    }

    public MethodSpec getGeneratedMethod(final MethodType type) {
        return generatedMethods.get(type);
    }

    public void setTransformer(final FieldBasedObjectTransformer transformer) {
        this.transformer = transformer;
    }

    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    public void setGetter(final ExecutableElement getter) {
        this.getter = getter;
    }

    public void setSetter(final ExecutableElement setter) {
        this.setter = setter;
    }

    public void setFluentSetter(final ExecutableElement fluentSetter) {
        this.fluentSetter = fluentSetter;
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
}
