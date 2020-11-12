package com.github.mouse0w0.coffeemaker.template.impl2.tree;

public interface AnnotationHolder {

    BtList<BtAnnotation> getAnnotations();

    default BtAnnotation getAnnotation(String descriptor) {
        for (BtAnnotation annotation : getAnnotations()) {
            if (descriptor.equals(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                return annotation;
            }
        }
        return null;
    }
}
