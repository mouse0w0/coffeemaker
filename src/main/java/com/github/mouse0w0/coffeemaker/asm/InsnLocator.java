package com.github.mouse0w0.coffeemaker.asm;

import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class InsnLocator {

    private String methodName;
    private String methodDesc;
    private int insnIndex;

    public InsnLocator(MethodNode method, AbstractInsnNode insn) {
        this(method.name, method.desc, method.instructions.indexOf(insn));
    }

    private InsnLocator(String methodName, String methodDesc, int insnIndex) {
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.insnIndex = insnIndex;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getMethodId() {
        return methodName + methodDesc;
    }

    public int getInsnIndex() {
        return insnIndex;
    }

    public AbstractInsnNode get(ClassNode classNode) {
        MethodNode methodNode = ASMUtils.getMethod(classNode, methodName, methodDesc);
        return methodNode != null ? methodNode.instructions.get(insnIndex) : null;
    }

    public AbstractInsnNode get(MethodNode methodNode) {
        if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
            return methodNode.instructions.get(insnIndex);
        }
        return null;
    }
}
