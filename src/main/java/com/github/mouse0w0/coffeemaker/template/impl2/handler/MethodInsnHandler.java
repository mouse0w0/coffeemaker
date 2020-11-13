package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtMethodInsn;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public abstract class MethodInsnHandler implements Handler {

    private final Set<String> methods = new HashSet<>();

    public MethodInsnHandler() {
        for (Method method : getAcceptableMethods()) {
            methods.add(getMethodId(method));
        }
    }

    protected abstract Method[] getAcceptableMethods();

    protected abstract void handle(BtMethod method, BtInsnNode insn);

    private static String getMethodId(Method method) {
        return Type.getInternalName(method.getDeclaringClass()) + "." + method.getName() + Type.getMethodDescriptor(method);
    }

    private static String getMethodId(BtInsnNode insn) {
        return insn.get(BtMethodInsn.OWNER).getAsString() + "." +
                insn.get(BtMethodInsn.NAME).getAsString() +
                insn.get(BtMethodInsn.DESCRIPTOR).getAsString();
    }

    @Override
    public void handle(BtClass clazz) {
        for (BtMethod method : clazz.getMethods()) {
            BtInsnList instructions = method.getInstructions();
            for (BtInsnNode insn : instructions) {
                if (insn instanceof BtMethodInsn && methods.contains(getMethodId(insn))) {
                    handle(method, insn);
                }
            }
        }
    }
}
