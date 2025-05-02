package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import de.bethibande.serial.processor.attributes.AttributeKey;
import de.bethibande.serial.processor.context.SerializationContext;
import de.bethibande.serial.processor.generator.FieldInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class EmbeddedTypeTransformer implements FieldBasedObjectTransformer {

    public static final AttributeKey<FieldInfo> COMPONENT_INFO = AttributeKey.of("embedded.componentInfo");

    protected abstract boolean isApplicable0(FieldInfo field, final ElementSerializer serializer);

    protected abstract FieldInfo createComponentInfo(final FieldInfo field);

    @Override
    public boolean isApplicable(final FieldInfo field, final ElementSerializer serializer) {
        if (!isApplicable0(field, serializer)) return false;

        if (field.get(COMPONENT_INFO).isPresent()) throw new IllegalStateException("EmbeddedTypeTransformer is already applicable");

        final FieldInfo componentInfo = createComponentInfo(field);
        // This will override the attribute map otherwise field and componentInfo will share their attribute map
        // which will cause an infinite loop when the component info of field is set.
        componentInfo.setTransformer(serializer.getFieldTransformer(componentInfo).orElseThrow());
        field.set(
                COMPONENT_INFO,
                componentInfo
        );

        if (field.equals(componentInfo)) throw new IllegalStateException("Field and component cannot be equal");

        return true;
    }

    protected FieldInfo getComponentInfo(final FieldInfo field) {
        return field.get(COMPONENT_INFO).orElseThrow();
    }

    protected abstract String childMethodSuffix();

    @Override
    public List<MethodSpec> createSerializerMethods(final FieldInfo field, final SerializationContext ctx) {
        final MethodSpec serializer = this.createSerializerMethod(field, ctx);
        final List<MethodSpec> methods = new ArrayList<>();
        methods.add(serializer);
        methods.addAll(this.createChildSerializers(field, ctx));

        return methods;
    }

    protected List<MethodSpec> createChildSerializers(final FieldInfo field, final SerializationContext ctx) {
        final FieldInfo componentInfo = getComponentInfo(field);
        final FieldBasedObjectTransformer childTransformer = componentInfo.getTransformer();
        final List<MethodSpec> methods = new ArrayList<>(childTransformer.createSerializerMethods(componentInfo, ctx));
        methods.set(0, this.rename(methods.getFirst(), childMethodSuffix()));

        return methods;
    }

    protected MethodSpec rename(final MethodSpec method, final String suffix) {
        final TypeName returnType = method.returnType();
        return method.toBuilder()
                .setName(method.name() + suffix)
                .returns(returnType)
                .build();
    }

    @Override
    public List<MethodSpec> createDeserializerMethods(final FieldInfo field, final SerializationContext ctx) {
        final MethodSpec deserializer = this.createDeserializerMethod(field, ctx);
        final List<MethodSpec> methods = new ArrayList<>();
        methods.add(deserializer);
        methods.addAll(this.createChildDeserializers(field, ctx));

        return methods;
    }

    protected List<MethodSpec> createChildDeserializers(final FieldInfo field, final SerializationContext ctx) {
        final FieldInfo componentInfo = getComponentInfo(field);
        final FieldBasedObjectTransformer childTransformer = componentInfo.getTransformer();
        final List<MethodSpec> methods = new ArrayList<>(childTransformer.createDeserializerMethods(componentInfo, ctx));
        methods.set(0, this.rename(methods.getFirst(), childMethodSuffix()));

        return methods;
    }
}
