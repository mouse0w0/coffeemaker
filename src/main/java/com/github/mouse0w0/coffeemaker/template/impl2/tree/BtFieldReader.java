package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

class BtFieldReader extends FieldVisitor {
    private final BtField field;

    public BtFieldReader(int api, BtField field) {
        super(api);
        this.field = field;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        BtAnnotation annotation = new BtAnnotation(descriptor, visible);
        field.computeIfNull(BtField.ANNOTATIONS, k -> new BtList())
                .add(annotation);
        return new BtAnnotationReader(api, annotation);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        field.computeIfNull(BtField.ATTRIBUTES, k -> new BtList())
                .addValue(attribute);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }
}
