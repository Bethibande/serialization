package de.bethibande.serial.processor.generator;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import de.bethibande.serial.processor.TypeHelper;
import de.bethibande.serial.processor.serializer.FieldBasedObjectTransformer;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

public class FieldInfo {

    private final String fieldName;
    private final TypeMirror type;

    private ExecutableElement getter;
    private ExecutableElement setter;
    private ExecutableElement fluentSetter;

    private boolean nullable = false;

    private FieldBasedObjectTransformer transformer;
    private Map<MethodType, MethodSpec> generatedMethods = new HashMap<>();

    public FieldInfo(final String fieldName, final TypeMirror type) {
        this.fieldName = fieldName;
        this.type = type;
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

}
