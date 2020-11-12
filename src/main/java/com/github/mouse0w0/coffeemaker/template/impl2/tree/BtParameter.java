package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import org.objectweb.asm.MethodVisitor;

public class BtParameter extends BtObject {
    public static final String NAME = "name";
    public static final String ACCESS = "access";

    public static final String ANNOTATIONS = "annotations";
    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public BtParameter(String name, int access) {
        putValue(NAME, name);
        putValue(ACCESS, access);
    }

    public BtParameter() {
    }

    public void acceptParameterAnnotation(MethodVisitor methodVisitor, int i, boolean b) {
        throw new UnsupportedOperationException();
    }
}
