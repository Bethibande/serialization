package de.bethibande.serial.example.subtypes;

import de.bethibande.serial.Reader;
import de.bethibande.serial.Writer;
import de.bethibande.serial.netty.NettyReader;
import de.bethibande.serial.netty.NettyWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class SubTypeExample {

    public static void main(String[] args) {
        final TypeASerializer typeASerializer = new TypeASerializer();
        final TypeADeserializer typeADeserializer = new TypeADeserializer();

        final TypeA objA = new TypeA();
        final TypeB objB = new TypeB();

        objB.setSomeInteger(123);
        objA.setValue(objB);

        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(32);
        final Writer writer = new NettyWriter(buf);
        final Reader reader = new NettyReader(buf);

        typeASerializer.bind(writer).write(objA);
        final TypeA result = typeADeserializer.bind(reader).read(new TypeA());

        System.out.println(result);
    }

}
