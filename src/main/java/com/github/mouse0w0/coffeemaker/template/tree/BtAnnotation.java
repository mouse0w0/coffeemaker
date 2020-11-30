package com.github.mouse0w0.coffeemaker.template.tree;

import com.github.mouse0w0.coffeemaker.evaluator.EmptyEvaluator;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtAnnotation extends BtObject {
    public static final BtAnnotation[] EMPTY = new BtAnnotation[0];

    public static final String DESCRIPTOR = "descriptor";
    public static final String VISIBLE = "visible";
    public static final String VALUES = "values";

    public BtAnnotation(String descriptor, boolean visible) {
        putValue(DESCRIPTOR, descriptor);
        putValue(VISIBLE, visible);
    }

    public BtAnnotation() {
    }

    public BtObject getValues() {
        return computeIfNull(BtAnnotation.VALUES, k -> new BtObject());
    }

    public void putAnnotationValue(String name, BtNode value) {
        getValues().put(name, value);
    }

    public void putAnnotationValue(String name, Object value) {
        getValues().putValue(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        BtNode node = getValues().get(key);
        return node != null ? (T) node.compute(EmptyEvaluator.INSTANCE) : null;
    }

    public <T> T getValue(String key, T defaultValue) {
        T value = getValue(key);
        return value != null ? value : defaultValue;
    }

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        String desc = computeDescriptor(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(classVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(FieldVisitor fieldVisitor, Evaluator evaluator) {
        String desc = computeDescriptor(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(fieldVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        String desc = computeDescriptor(DESCRIPTOR, evaluator);
        boolean visible = computeBoolean(VISIBLE, evaluator);
        accept(methodVisitor.visitAnnotation(desc, visible), evaluator);
    }

    public void accept(AnnotationVisitor annotationVisitor, String name, Evaluator evaluator) {
        accept(annotationVisitor.visitAnnotation(name, computeDescriptor(DESCRIPTOR, evaluator)), evaluator);
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

    @SuppressWarnings("unchecked")
    public static void accept(
            final AnnotationVisitor annotationVisitor, final String name, final BtNode node, final Evaluator evaluator) {
        if (annotationVisitor == null) return;

        if (node instanceof BtEnum) {
            BtEnum anEnum = (BtEnum) node;
            annotationVisitor.visitEnum(name, anEnum.computeDescriptor(BtEnum.DESCRIPTOR, evaluator), anEnum.computeString(BtEnum.VALUE, evaluator));
        } else if (node instanceof BtAnnotation) {
            BtAnnotation annotationValue = (BtAnnotation) node;
            annotationValue.accept(annotationVisitor, name, evaluator);
        } else if (node instanceof BtList) {
            AnnotationVisitor arrayAnnotationVisitor = annotationVisitor.visitArray(name);
            if (arrayAnnotationVisitor != null) {
                BtList<BtNode> arrayValue = (BtList<BtNode>) node;
                for (int i = 0, n = arrayValue.size(); i < n; ++i) {
                    accept(arrayAnnotationVisitor, null, arrayValue.get(i), evaluator);
                }
                arrayAnnotationVisitor.visitEnd();
            }
        } else {
            annotationVisitor.visit(name, node.computeNonNull(evaluator));
        }
    }
}