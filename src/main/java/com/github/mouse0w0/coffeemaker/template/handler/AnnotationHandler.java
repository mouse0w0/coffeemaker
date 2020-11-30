package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.tree.*;
import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

public abstract class AnnotationHandler implements Handler {
    private final Set<String> annotations = new HashSet<>();

    public AnnotationHandler() {
        for (Class<?> annotation : getAcceptableAnnotations()) {
            annotations.add(Type.getDescriptor(annotation));
        }
    }

    protected abstract Class<?>[] getAcceptableAnnotations();

    protected abstract void handle(AnnotationOwner owner, BtAnnotation annotation);

    @Override
    public void handle(BtClass clazz) {
        for (BtAnnotation annotation : clazz.getAnnotations().toArray(BtAnnotation.EMPTY)) {
            if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                handle(clazz, annotation);
            }
        }

        for (BtField field : clazz.getFields().toArray(BtField.EMPTY)) {
            for (BtAnnotation annotation : field.getAnnotations().toArray(BtAnnotation.EMPTY)) {
                if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                    handle(field, annotation);
                }
            }
        }

        for (BtMethod method : clazz.getMethods().toArray(BtMethod.EMPTY)) {
            for (BtAnnotation annotation : method.getAnnotations().toArray(BtAnnotation.EMPTY)) {
                if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                    handle(method, annotation);
                }
            }
        }
    }
}
