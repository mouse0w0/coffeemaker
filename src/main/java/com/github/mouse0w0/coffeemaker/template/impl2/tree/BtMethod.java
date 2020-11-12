package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class BtMethod extends BtObject {

    public static final String ACCESS = "access";
    public static final String NAME = "name";
    public static final String DESCRIPTOR = "descriptor";
    public static final String SIGNATURE = "signature";
    public static final String EXCEPTIONS = "exceptions";

    public static final String PARAMETERS = "parameters";

    public static final String ANNOTATIONS = "annotations";
    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public static final String ATTRIBUTES = "attributes";

    public static final String ANNOTATION_DEFAULT = "annotationDefault";

    public static final String INSTRUCTIONS = "instructions";

    public static final String TRY_CATCH_BLOCKS = "tryCatchBlocks";

    public static final String MAX_STACK = "maxStack";
    public static final String MAX_LOCALS = "maxLocals";

    public static final String LOCAL_VARIABLES = "localVariables";

    private boolean visited = true;

    public BtMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        putValue(ACCESS, access);
        putValue(NAME, name);
        putValue(DESCRIPTOR, descriptor);
        putValue(SIGNATURE, signature);
        putValue(EXCEPTIONS, new SmartList<>(exceptions));
    }

    public BtMethod() {
    }

    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        MethodVisitor methodVisitor =
                classVisitor.visitMethod(
                        computeInt(ACCESS, evaluator),
                        computeString(NAME, evaluator),
                        computeString(DESCRIPTOR, evaluator),
                        computeString(SIGNATURE, evaluator),
                        computeStringArray(EXCEPTIONS, evaluator));

        if (methodVisitor == null) {
            return;
        }

        // Visit the parameters.
        BtList parameters = get(PARAMETERS);
        if (parameters != null) {
            for (BtNode node : parameters) {
                BtParameter parameter = (BtParameter) node;
                methodVisitor.visitParameter(parameter.computeString(BtParameter.NAME, evaluator),
                        parameter.computeInt(BtParameter.ACCESS, evaluator));
            }
        }
        // Visit the annotations.
        BtNode annotationDefault = get(ANNOTATION_DEFAULT);
        if (annotationDefault != null) {
            AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
            if (annotationVisitor != null) {
                BtAnnotation.accept(annotationVisitor, null, annotationDefault, evaluator);
                annotationVisitor.visitEnd();
            }
        }
        if (containsKey(ANNOTATIONS)) {
            BtList annotations = get(ANNOTATIONS);
            for (BtNode node : annotations) {
                ((BtAnnotation) node).accept(methodVisitor, evaluator);
            }
        }

//        if (typeAnnotations != null) {
//            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
//                typeAnnotation.accept(
//                        methodVisitor.visitTypeAnnotation(
//                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
//            }
//        }

        int visibleAnnotableParameterCount = 0;
        int invisibleAnnotableParameterCount = 0;
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                BtParameter parameter = (BtParameter) parameters.get(i);
                BtList annotations = (BtList) parameter.compute(BtParameter.ANNOTATIONS, evaluator);
                if (annotations == null) continue;

                for (BtNode annotation : annotations) {
                    if (((BtAnnotation) annotation).computeBoolean(BtAnnotation.VISIBLE, evaluator)) {
                        visibleAnnotableParameterCount = i + 1;
                    } else {
                        invisibleAnnotableParameterCount = i + 1;
                    }
                }
            }
        }

        if (visibleAnnotableParameterCount > 0) {
            methodVisitor.visitAnnotableParameterCount(visibleAnnotableParameterCount, true);
            for (int i = 0; i < visibleAnnotableParameterCount; i++) {
                BtParameter parameter = (BtParameter) parameters.get(i);
                parameter.acceptParameterAnnotation(methodVisitor, i, true);
            }
        }
        if (invisibleAnnotableParameterCount > 0) {
            methodVisitor.visitAnnotableParameterCount(invisibleAnnotableParameterCount, false);
            for (int i = 0; i < visibleAnnotableParameterCount; i++) {
                BtParameter parameter = (BtParameter) parameters.get(i);
                parameter.acceptParameterAnnotation(methodVisitor, i, false);
            }
        }
        // Visit the non standard attributes.
        BtInsnList instructions = get(INSTRUCTIONS);
        if (visited) {
            instructions.resetLabels();
        }
        if (containsKey(ATTRIBUTES)) {
            BtList attributes = get(ATTRIBUTES);
            for (BtNode node : attributes) {
                classVisitor.visitAttribute(node.computeAttribute(evaluator));
            }
        }
        // Visit the code.
        if (instructions.size() > 0) {
            methodVisitor.visitCode();
            // Visits the try catch blocks.

            BtList tryCatchBlocks = get(TRY_CATCH_BLOCKS);
            if (tryCatchBlocks != null) {
                for (int i = 0, n = tryCatchBlocks.size(); i < n; ++i) {
                    BtTryCatchBlock tryCatchBlock = (BtTryCatchBlock) tryCatchBlocks.get(i);
                    tryCatchBlock.updateIndex(i);
                    tryCatchBlock.accept(methodVisitor, evaluator);
                }
            }
            // Visit the instructions.
            instructions.accept(methodVisitor, evaluator);
            // Visits the local variables.
//            if (localVariables != null) {
//                for (int i = 0, n = localVariables.size(); i < n; ++i) {
//                    localVariables.get(i).accept(methodVisitor);
//                }
//            }
//            // Visits the local variable annotations.
//            if (visibleLocalVariableAnnotations != null) {
//                for (int i = 0, n = visibleLocalVariableAnnotations.size(); i < n; ++i) {
//                    visibleLocalVariableAnnotations.get(i).accept(methodVisitor, true);
//                }
//            }
//            if (invisibleLocalVariableAnnotations != null) {
//                for (int i = 0, n = invisibleLocalVariableAnnotations.size(); i < n; ++i) {
//                    invisibleLocalVariableAnnotations.get(i).accept(methodVisitor, false);
//                }
//            }
            methodVisitor.visitMaxs(computeInt(MAX_STACK, evaluator), computeInt(MAX_LOCALS, evaluator));
            visited = true;
        }
        methodVisitor.visitEnd();
    }
}
