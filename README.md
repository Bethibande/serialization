# Java Serialization

This project aims to create a low-latency serialization framework for Java.

> [!CAUTION]
> This framework is still very much work-in-progress. Non-experimental usage is not yet recommended.

### Example

```java
import java.nio.ByteBuffer;

@Getter
@Setter
@SerializableType
public class MyClass {

    private int someInteger;
    private boolean someBoolean;
    private String someString;

}

public static void main(String[] args) {
    final ByteBuffer buffer = ByteBuffer.allocateDirect(64);
    final Writer writer = new ByteBufferWriter(buffer);
    final Reader reader = new ByteBufferReader(buffer);

    final MyClass myObject = new MyClass();
    myObject.setSomeInteger(123);
    myObject.setSomeBoolean(true);
    myObject.setSomestring("abc");

    final MyClassSerializer serializer = new MyClassSerializer();
    serializer.bind(writer).write(myObject);

    buffer.flip();

    final MyClassDeserializer deserializer = new MyClassDeserializer();
    final MyClass output = deserializer.bind(reader).read(new MyClass());
}
```

For more examples see [here](/example/src/main/java/de/bethibande/serial/example/Main.java)

### Type support

- ✅ Fully supported
- 💡Coming soon
- ❌ Not yet supported

| Type                     | Support |
|--------------------------|---------|
| Any primitive type       | ✅       |
| Any boxed primitive type | ✅       |
| String / CharSequence    | ✅       |
| Any enum type            | ✅       |
| Arrays                   | ✅       |
| Collections              | ✅       |
| Maps                     | 💡      |
| Other serializable types | ❌       |
| Java time types          | ❌       |

Please note that not null annotations are supported. Marking nullable fields as not nullable will omit null-checks,
doing so can cause null pointer exceptions at runtime in the event that such a field does contain a null value.
However, serializing/deserializing not nullable fields is faster.