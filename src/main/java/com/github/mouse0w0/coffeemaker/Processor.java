package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.asm.extree.ClassNodeEx;

public interface Processor {
    void process(ClassNodeEx classNode, Evaluator evaluator);
}
