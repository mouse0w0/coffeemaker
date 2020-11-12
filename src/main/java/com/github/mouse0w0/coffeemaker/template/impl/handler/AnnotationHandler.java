package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import com.github.mouse0w0.coffeemaker.template.impl.extree.AnnotationHolder;
import com.github.mouse0w0.coffeemaker.template.impl.extree.AnnotationNodeEx;

import java.util.List;

public interface AnnotationHandler {
    String[] getSupportedAnnotationTypes();

    void handle(AnnotationHolder owner, AnnotationNodeEx annotation, List<Processor> processors);
}
