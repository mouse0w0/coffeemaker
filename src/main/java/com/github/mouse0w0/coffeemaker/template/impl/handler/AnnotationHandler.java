package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.extree.AnnotationHolder;
import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;

import java.util.List;

public interface AnnotationHandler {
    String[] getSupportedAnnotationTypes();

    void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors);
}
