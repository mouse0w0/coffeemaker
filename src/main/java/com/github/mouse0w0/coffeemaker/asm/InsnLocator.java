package com.github.mouse0w0.coffeemaker.asm;

import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class InsnLocator {

    private MethodIdentifier method;
    private int insnIndex;

    public InsnLocator(MethodNode method, AbstractInsnNode insn) {
        this(method.name, method.desc, method.instructions.indexOf(insn));
    }

    private InsnLocator(String methodName, String methodDesc, int insnIndex) {
        this.method = new MethodIdentifier(null, methodName, methodDesc);
        this.insnIndex = insnIndex;
    }

    public MethodIdentifier getMethod() {
        return method;
    }

    public int getInsnIndex() {
        return insnIndex;
    }

    public AbstractInsnNode get(ClassNode classNode) {
        MethodNode methodNode = ASMUtils.getMethod(classNode, method.getName(), method.getDescriptor());
        return methodNode != null ? methodNode.instructions.get(insnIndex) : null;
    }

    public AbstractInsnNode get(MethodNode methodNode) {
        if (method.equals(methodNode)) {
            return methodNode.instructions.get(insnIndex);
        }
        return null;
    }
}
