package com.github.mouse0w0.coffeemaker.impl.handler;

import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.asm.AnnotationNodeEx;

import java.util.List;

public interface AnnotationHandler {
    String[] getSupportedAnnotationTypes();

    void handle(Object owner, AnnotationNodeEx annotation, List<Processor> processors);
}
