package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class ModifyAnnotationProcessor implements Processor {

    public static Processor.Factory factory() {
        return new Factory();
    }

    @Override
    public void process(ClassNode classNode, Evaluator evaluator) {

    }

    private static class Factory implements Processor.Factory {

        @Override
        public void create(ClassNode classNode, List<Processor> processors) {

        }
    }
}
