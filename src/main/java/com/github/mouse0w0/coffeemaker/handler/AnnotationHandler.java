package com.github.mouse0w0.coffeemaker.handler;

import com.github.mouse0w0.coffeemaker.processor.Processor;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;
import java.util.Set;

public interface AnnotationHandler {
    Set<String> getSupportedAnnotationTypes();

    void handle(Object owner, AnnotationNode annotation, List<Processor> processors);
}
