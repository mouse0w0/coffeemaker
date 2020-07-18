package com.github.mouse0w0.coffeemaker.processor;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;

public interface Processor {
    void process(ClassNodeEx classNode, Evaluator evaluator);
}
