package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.MethodVisitor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterNodeEx {
    /**
     * The parameter's name.
     */
    public String name;

    /**
     * The parameter's access flags (see {@link org.objectweb.asm.Opcodes}). Valid values are {@code
     * ACC_FINAL}, {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     */
    public int access;

    public Map<String, AnnotationNodeEx> annotations;

    /**
     * Constructs a new {@link ParameterNodeEx}.
     *
     * @param access The parameter's access flags. Valid values are {@code ACC_FINAL}, {@code
     *               ACC_SYNTHETIC} or/and {@code ACC_MANDATED} (see {@link org.objectweb.asm.Opcodes}).
     * @param name   the parameter's name.
     */
    public ParameterNodeEx(final String name, final int access) {
        this.name = name;
        this.access = access;
    }

    public AnnotationNodeEx getAnnotation(String descriptor) {
        return annotations == null ? null : annotations.get(descriptor);
    }

    public void addAnnotation(AnnotationNodeEx annotationNode) {
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(annotationNode.desc, annotationNode);
    }

    /**
     * Makes the given visitor visit this parameter declaration.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitParameter(name, access);
    }

    public void acceptParameterAnnotation(final MethodVisitor methodVisitor, final int parameter, final boolean visible) {
        if (annotations == null) return;

        Collection<AnnotationNodeEx> annotations = this.annotations.values();
        for (AnnotationNodeEx annotation : annotations) {
            if (annotation.visible == visible) {
                annotation.accept(methodVisitor.visitParameterAnnotation(parameter, annotation.desc, visible));
            }
        }
    }
}
