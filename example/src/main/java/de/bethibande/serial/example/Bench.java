package de.bethibande.serial.example;

import de.bethibande.serial.Reader;
import de.bethibande.serial.Writer;
import de.bethibande.serial.netty.NettyReader;
import de.bethibande.serial.netty.NettyWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
public class Bench {
    private final ExampleTypeDTO dto = new ExampleTypeDTO()
            .withSomeInteger(123)
            .withSomeLong(456L);

    private ByteBuf buffer;
    private Writer writer;
    private Reader reader;

    @Setup
    public void setup() {
        buffer = ByteBufAllocator.DEFAULT.directBuffer(12);

        writer = new NettyWriter(buffer);
        reader = new NettyReader(buffer);

        dto.write(writer);
    }

    @Benchmark
    public void writeLoop() {
        buffer.clear();
        dto.write(writer);
    }

    @Benchmark
    public void readLoop() {
        buffer.resetReaderIndex();
        dto.read(reader);
        // Prevent DCE
        if (dto.someInteger() + dto.someLong() == 0) throw new AssertionError();
    }

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }
}