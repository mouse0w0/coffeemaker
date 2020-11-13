package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import org.objectweb.asm.AnnotationVisitor;

class BtAnnotationVisitor extends AnnotationVisitor {
    private BtAnnotation annotation;
    private BtList<BtNode> list;

    public BtAnnotationVisitor(int api) {
        super(api);
    }

    public BtAnnotationVisitor(int api, BtAnnotation annotation) {
        super(api);
        this.annotation = annotation;
    }

    public BtAnnotationVisitor(int api, BtList<BtNode> list) {
        super(api);
        this.list = list;
    }

    protected void put(String name, BtNode value) {
        if (annotation != null) {
            annotation.putAnnotationValue(name, value);
        } else {
            list.add(value);
        }
    }

    protected void putValue(String name, Object value) {
        if (annotation != null) {
            annotation.putAnnotationValue(name, value);
        } else {
            list.addValue(value);
        }
    }

    @Override
    public void visit(String name, Object value) {
        putValue(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        put(name, new BtEnum(descriptor, value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        BtAnnotation annotation = new BtAnnotation(descriptor, false);
        put(name, annotation);
        return new BtAnnotationVisitor(api, annotation);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        BtList<BtNode> list = new BtList<>();
        put(name, list);
        return new BtAnnotationVisitor(api, list);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }
}
