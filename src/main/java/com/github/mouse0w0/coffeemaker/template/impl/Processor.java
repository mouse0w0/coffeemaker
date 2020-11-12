package com.github.mouse0w0.coffeemaker.template.impl;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl.extree.ClassNodeEx;

public interface Processor {
    ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator);
}
