package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class BtField extends BtObject implements AnnotationOwner {
    public static final String ACCESS = "access";
    public static final String NAME = "name";
    public static final String DESCRIPTOR = "descriptor";
    public static final String SIGNATURE = "signature";
    public static final String VALUE = "value";

    public static final String ANNOTATIONS = "annotations";

    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public static final String ATTRIBUTES = "attributes";

    public BtField(int access, String name, String descriptor, String signature, Object value) {
        putValue(ACCESS, access);
        putValue(NAME, name);
        putValue(DESCRIPTOR, descriptor);
        putValue(SIGNATURE, signature);
        putValue(VALUE, value);
    }

    public BtField() {
    }

    @Override
    public BtList<BtAnnotation> getAnnotations() {
        return computeIfNull(ANNOTATIONS, k -> new BtList<>());
    }

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        FieldVisitor fieldVisitor = classVisitor.visitField(
                computeInt(ACCESS, evaluator),
                computeString(NAME, evaluator),
                computeDescriptor(DESCRIPTOR, evaluator),
                computeString(SIGNATURE, evaluator),
                compute(VALUE, evaluator));
        if (fieldVisitor == null) {
            return;
        }
        accept(fieldVisitor, evaluator);
    }

    protected void accept(FieldVisitor fieldVisitor, Evaluator evaluator) {
        // Visit the annotations.
        BtList<BtAnnotation> annotations = getAnnotations();
        if (annotations != null) {
            for (BtAnnotation node : annotations) {
                node.accept(fieldVisitor, evaluator);
            }
        }
//        if (typeAnnotations != null) {
//            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
//                typeAnnotation.accept(
//                        fieldVisitor.visitTypeAnnotation(
//                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
//            }
//        }
        // Visit the non standard attributes.
        if (containsKey(ATTRIBUTES)) {
            BtList<BtNode> attributes = get(ATTRIBUTES);
            for (BtNode node : attributes) {
                fieldVisitor.visitAttribute(node.computeAttribute(evaluator));
            }
        }

        fieldVisitor.visitEnd();
    }
}
