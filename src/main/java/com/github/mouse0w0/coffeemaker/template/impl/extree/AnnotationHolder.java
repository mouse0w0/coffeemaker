package com.github.mouse0w0.coffeemaker.template.impl.extree;

import java.util.Collection;

public interface AnnotationHolder {
    Collection<AnnotationNodeEx> getAnnotations();

    AnnotationNodeEx getAnnotation(String descriptor);

    void addAnnotation(AnnotationNodeEx annotation);

    void removeAnnotation(String descriptor);

    void removeAnnotation(AnnotationNodeEx annotation);
}
