package de.bethibande.serial.example;

import de.bethibande.serial.Reader;
import de.bethibande.serial.Writer;
import de.bethibande.serial.buffer.ByteBufferReader;
import de.bethibande.serial.buffer.ByteBufferWriter;
import de.bethibande.serial.netty.NettyReader;
import de.bethibande.serial.netty.NettyWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
@Threads(Threads.MAX)
public class Bench {

    private final TestDTO dto = new TestDTO().withSomeNumber(234).withSomeString("abc");

    private ByteBuf nettyBuffer;
    private ByteBuffer javaBuffer;

    private Writer nettyWriter;
    private Reader nettyReader;

    private Writer javaWriter;
    private Reader javaReader;

    private TestDTOSerializer serializer;
    private TestDTODeserializer deserializer;

    @Setup
    public void setup() {
        nettyBuffer = ByteBufAllocator.DEFAULT.directBuffer(32);
        javaBuffer = ByteBuffer.allocateDirect(32);

        nettyWriter = new NettyWriter(nettyBuffer);
        nettyReader = new NettyReader(nettyBuffer);

        javaWriter = new ByteBufferWriter(javaBuffer);
        javaReader = new ByteBufferReader(javaBuffer);

        serializer = new TestDTOSerializer();
        deserializer = new TestDTODeserializer();

        serializer.bind(nettyWriter).write(dto);
        serializer.bind(javaWriter).write(dto);
    }

    @Benchmark
    public void writeLoopNetty(final Blackhole blackhole) {
        nettyBuffer.clear();
        serializer.bind(nettyWriter).write(dto);

        blackhole.consume(nettyBuffer);
    }

    @Benchmark
    public void readLoopNetty(final Blackhole blackhole) {
        nettyBuffer.resetReaderIndex();
        deserializer.bind(nettyReader).read(dto);

        blackhole.consume(dto);
    }

    @Benchmark
    public void writeLoopJava(final Blackhole blackhole) {
        javaBuffer.flip();
        serializer.bind(javaWriter).write(dto);

        blackhole.consume(javaBuffer);
    }

    @Benchmark
    public void readLoopJava(final Blackhole blackhole) {
        javaBuffer.flip();
        deserializer.bind(javaReader).read(dto);

        blackhole.consume(dto);
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}