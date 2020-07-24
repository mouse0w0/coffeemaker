package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;

import java.util.List;

public interface Processor {
    void process(ClassNodeEx classNode, Evaluator evaluator);

    interface Factory {
        void create(ClassNodeEx classNode, List<Processor> processors);
    }
}
