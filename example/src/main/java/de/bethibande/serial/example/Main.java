package de.bethibande.serial.example;

import de.bethibande.serial.Reader;
import de.bethibande.serial.Writer;
import de.bethibande.serial.netty.NettyReader;
import de.bethibande.serial.netty.NettyWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class Main {

    private static long testWrite(final ByteBuf buffer, final ExampleTypeDTO dto, final int iterations) {
        final Writer writer = new NettyWriter(buffer);
        final long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            buffer.clear();
            dto.write(writer);
        }
        return System.currentTimeMillis() - start;
    }

    private static void testWriteAndPrint(final ByteBuf buffer, final ExampleTypeDTO dto, final int iterations) {
        final long time = testWrite(buffer, dto, iterations);
        printResults(time, iterations);
    }

    private static long testRead(final ByteBuf buffer, final ExampleTypeDTO dto, final int iterations) {
        final Reader reader = new NettyReader(buffer);
        final long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            buffer.resetReaderIndex();
            dto.read(reader);
        }
        return System.currentTimeMillis() - start;
    }

    private static void testReadAndPrint(final ByteBuf buffer, final ExampleTypeDTO dto, final int iterations) {
        final long time = testRead(buffer, dto, iterations);
        printResults(time, iterations);
    }

    private static void printResults(final long time, final int iterations) {
        final double msPerOperation = time / (double) iterations;
        final double nsPerOperation = msPerOperation * 1_000_000;

        final int operationsPerSecond = (int) (iterations / msPerOperation);

        System.out.println("Time: " + time + "ms | " + formatDouble(nsPerOperation) + " ns/op | " + formatInt(operationsPerSecond) + " ops/s");
    }

    private static String formatInt(final int value) {
        return String.format("%,d", value);
    }

    private static String formatDouble(final double value) {
        return String.format("%.2f", value);
    }

    public static void main(String[] args) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(12);
        final ExampleTypeDTO dto = new ExampleTypeDTO()
                .withSomeInteger(123)
                .withSomeLong(456L);

        final ExampleType snap = dto.snapshot();
        // TODO: Auto-expand buffer when calling write(Writer) method.

        final int warmupIterations = 1_000_000;
        final int runIterations = 2_000_000_000;

        testWrite(buffer, dto, warmupIterations);
        testRead(buffer, dto, warmupIterations);

        testWriteAndPrint(buffer, dto, runIterations);
        testReadAndPrint(buffer, dto, runIterations);
    }

}
