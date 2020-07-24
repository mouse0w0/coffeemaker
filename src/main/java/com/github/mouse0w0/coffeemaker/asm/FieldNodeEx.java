package com.github.mouse0w0.coffeemaker.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.tree.FieldNode;

import java.util.HashMap;
import java.util.Map;

public class FieldNodeEx extends FieldNode {
    private final Map<String, AnnotationNodeEx> annotationsEx = new HashMap<>();

    public FieldNodeEx(int access, String name, String descriptor, String signature, Object value) {
        super(access, name, descriptor, signature, value);
    }

    public FieldNodeEx(int api, int access, String name, String descriptor, String signature, Object value) {
        super(api, access, name, descriptor, signature, value);
    }

    public AnnotationNodeEx getAnnotationEx(String descriptor) {
        return annotationsEx.get(descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor);
        annotation.visible = visible;
        if (visible) {
            visibleAnnotations = Util.add(visibleAnnotations, annotation);
        } else {
            invisibleAnnotations = Util.add(invisibleAnnotations, annotation);
        }
        annotationsEx.put(descriptor, annotation);
        return annotation;
    }
}
