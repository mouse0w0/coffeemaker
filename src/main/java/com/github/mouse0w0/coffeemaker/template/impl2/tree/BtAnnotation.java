package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.Enum;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtAnnotation extends BtObject {
    public static final String DESCRIPTOR = "descriptor";
    public static final String VISIBLE = "visible";
    public static final String VALUES = "values";

    public BtAnnotation(String descriptor, boolean visible) {
        putValue(DESCRIPTOR, descriptor);
        putValue(VISIBLE, visible);
    }

    public BtAnnotation() {
    }

    public void putAnnotationValue(String name, BtNode value) {
        computeIfNull(BtAnnotation.VALUES, k -> new BtObject())
                .put(name, value);
    }

    public void putAnnotationValue(String name, Object value) {
        computeIfNull(BtAnnotation.VALUES, k -> new BtObject())
                .putValue(name, value);
    }

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        String desc = computeString(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(classVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(FieldVisitor fieldVisitor, Evaluator evaluator) {
        String desc = computeString(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(fieldVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        String desc = computeString(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(methodVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(AnnotationVisitor annotationVisitor, String name, Evaluator evaluator) {
        accept(annotationVisitor.visitAnnotation(name, computeString(DESCRIPTOR, evaluator)), evaluator);
    }

    public void accept(AnnotationVisitor annotationVisitor, Evaluator evaluator) {
        if (annotationVisitor == null) return;

        BtObject values = get(VALUES);
        if (values != null) {
            for (Map.Entry<String, BtNode> entry : values.entrySet()) {
                accept(annotationVisitor, entry.getKey(), entry.getValue(), evaluator);
            }
        }

        annotationVisitor.visitEnd();
    }

    public static void accept(
            final AnnotationVisitor annotationVisitor, final String name, final BtNode node, final Evaluator evaluator) {
        if (annotationVisitor == null) return;

        if (node instanceof BtAnnotation) {
            BtAnnotation annotationValue = (BtAnnotation) node;
            annotationValue.accept(annotationVisitor, name, evaluator);
        } else if (node instanceof BtList) {
            AnnotationVisitor arrayAnnotationVisitor = annotationVisitor.visitArray(name);
            if (arrayAnnotationVisitor != null) {
                BtList arrayValue = (BtList) node;
                for (int i = 0, n = arrayValue.size(); i < n; ++i) {
                    accept(arrayAnnotationVisitor, null, arrayValue.get(i), evaluator);
                }
                arrayAnnotationVisitor.visitEnd();
            }
        } else {
            Object value = node.compute(evaluator);
            if (value instanceof Enum) {
                Enum anEnum = (Enum) value;
                annotationVisitor.visitEnum(name, anEnum.getDescriptor(), anEnum.getValue());
            } else {
                annotationVisitor.visit(name, value);
            }
        }
    }
}