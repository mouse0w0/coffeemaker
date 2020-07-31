package com.github.mouse0w0.coffeemaker.impl.handler;

import com.github.mouse0w0.asm.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.Processor;

import java.util.List;

public interface AnnotationHandler {
    String[] getSupportedAnnotationTypes();

    void handle(Object owner, AnnotationNodeEx annotation, List<Processor> processors);
}
