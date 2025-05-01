package de.bethibande.serial.example;

import de.bethibande.serial.Reader;
import de.bethibande.serial.Writer;
import de.bethibande.serial.netty.NettyReader;
import de.bethibande.serial.netty.NettyWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class Main {

    public static void main(String[] args) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(12);
        final Writer writer = new NettyWriter(buffer);
        final Reader reader = new NettyReader(buffer);

        final TestDTO test = new TestDTO();
        test.setSomeString("abc");
        final TestDTOSerializer serializer = new TestDTOSerializer();
        final TestDTODeserializer deserializer = new TestDTODeserializer();

        serializer.bind(writer)
                .write(test.withSomeNumber(234))
                .write(test.withSomeNumber(123))
                .write(test.withSomeNumber(456))
                .write(test.withSomeNumber(789));

        final TestDTO dtoA = deserializer.bind(reader).read(new TestDTO());
        final TestDTO dtoB = deserializer.read(new TestDTO());
        final TestDTO dtoC = deserializer.read(new TestDTO());
        final TestDTO dtoD = deserializer.read(new TestDTO());

        System.out.println(dtoA);
        System.out.println(dtoB);
        System.out.println(dtoC);
        System.out.println(dtoD);
    }

}
