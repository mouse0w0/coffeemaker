package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.AnnotationNodeEx;
import com.github.mouse0w0.coffeemaker.extree.Enum;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

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

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {

    }

    public void accept(FieldVisitor fieldVisitor, Evaluator evaluator) {

    }

    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {

    }

    public void accept(AnnotationVisitor annotationVisitor, Evaluator evaluator) {

    }

    public static void accept(
            final AnnotationVisitor annotationVisitor, final String name, final Object value) {
        if (annotationVisitor == null) return;

        if (value instanceof Enum) {
            Enum anEnum = (Enum) value;
            annotationVisitor.visitEnum(name, anEnum.getDescriptor(), anEnum.getValue());
        } else if (value instanceof AnnotationNodeEx) {
            AnnotationNodeEx annotationValue = (AnnotationNodeEx) value;
            annotationValue.accept(annotationVisitor.visitAnnotation(name, annotationValue.desc));
        } else if (value instanceof List) {
            AnnotationVisitor arrayAnnotationVisitor = annotationVisitor.visitArray(name);
            if (arrayAnnotationVisitor != null) {
                List<?> arrayValue = (List<?>) value;
                for (int i = 0, n = arrayValue.size(); i < n; ++i) {
                    accept(arrayAnnotationVisitor, null, arrayValue.get(i));
                }
                arrayAnnotationVisitor.visitEnd();
            }
        } else {
            annotationVisitor.visit(name, value);
        }
    }
}
