package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.Enum;
import org.objectweb.asm.AnnotationVisitor;

class BtAnnotationReader extends AnnotationVisitor {
    private BtAnnotation annotation;
    private BtList list;

    public BtAnnotationReader(int api) {
        super(api);
    }

    public BtAnnotationReader(int api, BtAnnotation annotation) {
        super(api);
        this.annotation = annotation;
    }

    public BtAnnotationReader(int api, BtList list) {
        super(api);
        this.list = list;
    }

    public void putValue(String name, Object value) {
        if (annotation != null) {
            annotation.computeIfNull(BtAnnotation.VALUES, k -> new BtObject())
                    .putValue(name, value);
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
        putValue(name, new Enum(descriptor, value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        BtAnnotation annotation = new BtAnnotation(descriptor, false);
        putValue(name, annotation);
        return new BtAnnotationReader(api, annotation);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        BtList list = new BtList();
        putValue(name, list);
        return new BtAnnotationReader(api, list);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }
}
