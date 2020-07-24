package com.github.mouse0w0.coffeemaker.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;

public class MethodNodeEx extends MethodNode {
    private final Map<String, AnnotationNodeEx> annotationsEx = new HashMap<>();

    private AbstractInsnNode[] insnNodesEx;

    public MethodNodeEx() {
        this(Opcodes.ASM5);
    }

    public MethodNodeEx(int api) {
        super(api);
    }

    public MethodNodeEx(int access, String name, String descriptor, String signature, String[] exceptions) {
        super(access, name, descriptor, signature, exceptions);
    }

    public MethodNodeEx(int api, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(api, access, name, descriptor, signature, exceptions);
    }

    public AnnotationNodeEx getAnnotationEx(String descriptor) {
        return annotationsEx.get(descriptor);
    }

    public AbstractInsnNode[] getInsnNodesEx() {
        return insnNodesEx;
    }

    public AbstractInsnNode getInsnNodeEx(int index) {
        return insnNodesEx[index];
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor);
        annotation.visible = visible;
        if (visible) {
            visibleAnnotations = Util.add(visibleAnnotations, annotation);
        } else {
            invisibleAnnotations = Util.add(invisibleAnnotations, annotation);
        }
        annotationsEx.put(descriptor, annotation);
        return annotation;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        insnNodesEx = instructions.toArray();
    }
}
