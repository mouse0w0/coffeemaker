package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;

import java.util.List;

public interface AnnotationHandler {
    String[] getSupportedAnnotationTypes();

    void handle(Object owner, AnnotationNodeEx annotation, List<Processor> processors);
}
