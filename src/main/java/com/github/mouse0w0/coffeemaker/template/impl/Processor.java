package com.github.mouse0w0.coffeemaker.template.impl;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;

public interface Processor {
    void process(ClassNodeEx classNode, Evaluator evaluator);
}
