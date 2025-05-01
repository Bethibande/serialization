package de.bethibande.serial.processor.serializer;

import com.palantir.javapoet.CodeBlock;

import java.util.ArrayList;
import java.util.List;

public sealed interface SizeCalculator permits SizeCalculator.CodeBlockSizeCalculator,
        SizeCalculator.CombinedSizeCalculator,
        SizeCalculator.StaticSizeCalculator {

    static SizeCalculator ofStaticSize(final int size) {
        return new StaticSizeCalculator(size);
    }

    static SizeCalculator ofCodeBlock(final CodeBlock codeBlock) {
        return new CodeBlockSizeCalculator(codeBlock);
    }

    static SizeCalculator ofAll(final List<SizeCalculator> calculators) {
        return new CombinedSizeCalculator(calculators);
    }

    final class StaticSizeCalculator implements SizeCalculator {

        private final int size;

        public StaticSizeCalculator(final int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        @Override
        public CodeBlock toCodeBlock() {
            return CodeBlock.of("$L", this.size);
        }
    }

    final class CodeBlockSizeCalculator implements SizeCalculator {

        private final CodeBlock codeBlock;

        public CodeBlockSizeCalculator(final CodeBlock codeBlock) {
            this.codeBlock = codeBlock;
        }

        @Override
        public CodeBlock toCodeBlock() {
            return codeBlock;
        }
    }

    final class CombinedSizeCalculator implements SizeCalculator {

        private final List<SizeCalculator> calculators;

        public CombinedSizeCalculator(final List<SizeCalculator> calculators) {
            this.calculators = calculators;
        }


        @Override
        public CodeBlock toCodeBlock() {
            final int staticSize = calculators.stream()
                    .filter(it -> it instanceof StaticSizeCalculator)
                    .map(StaticSizeCalculator.class::cast)
                    .mapToInt(StaticSizeCalculator::getSize)
                    .sum();
            final SizeCalculator staticSumCalculator = ofStaticSize(staticSize);

            final List<SizeCalculator> calculators = new ArrayList<>();
            if (staticSize > 0) calculators.add(staticSumCalculator);
            calculators.addAll(this.calculators.stream()
                    .filter(it -> !(it instanceof StaticSizeCalculator))
                    .toList());

            return CodeBlock.join(
                    calculators.stream()
                            .map(SizeCalculator::toCodeBlock)
                            .toList(),
                    "\n+ "
            );
        }
    }

    CodeBlock toCodeBlock();

}
