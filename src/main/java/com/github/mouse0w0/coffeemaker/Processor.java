package com.github.mouse0w0.coffeemaker;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public interface Processor {
    void process(ClassNode classNode, Evaluator evaluator);

    interface Factory {
        void create(ClassNode classNode, List<Processor> processors);
    }
}
