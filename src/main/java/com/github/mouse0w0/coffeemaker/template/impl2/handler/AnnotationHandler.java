package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.*;
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
        for (BtAnnotation annotation : clazz.getAnnotations()) {
            if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                handle(clazz, annotation);
            }
        }

        for (BtField field : clazz.getFields()) {
            for (BtAnnotation annotation : field.getAnnotations()) {
                if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                    handle(field, annotation);
                }
            }
        }

        for (BtMethod method : clazz.getMethods()) {
            for (BtAnnotation annotation : method.getAnnotations()) {
                if (annotations.contains(annotation.get(BtAnnotation.DESCRIPTOR).getAsString())) {
                    handle(method, annotation);
                }
            }
        }
    }
}
