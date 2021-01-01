package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.tree.insn.BtFieldInsn;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.ArrayDeque;

public class Utils {
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameters) {
        try {
            return clazz.getDeclaredMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static BtInsnList subInsnList(BtInsnList insnList, BtInsnNode start, BtInsnNode end) {
        BtInsnList subInsnList = new BtInsnList();
        BtInsnNode insn = start;
        BtInsnNode next;
        while (insn != end && insn != null) {
            next = insn.getNext();
            insnList.remove(insn);
            subInsnList.add(insn);
            insn = next;
        }
        return subInsnList;
    }

    public static void removeRange(BtInsnList insnList, BtInsnNode start, BtInsnNode end) {
        BtInsnNode insn = start;
        BtInsnNode next;
        while (insn != end && insn != null) {
            next = insn.getNext();
            insnList.remove(insn);
            insn = next;
        }
    }

    public static BtInsnNode getMethodArgument(BtMethodInsn insn, int index) {
        ArrayDeque<BtInsnNode> stack = new ArrayDeque<>();
        BtInsnNode current = insn.getPrevious();
        for (int i = getMethodArgumentCount(insn.getDescriptor()) - index; current != null; current = current.getPrevious()) {
            if (current instanceof BtMethodInsn) {
                BtMethodInsn methodInsn = (BtMethodInsn) current;
                String descriptor = methodInsn.getDescriptor();
                i += getMethodArgumentCount(descriptor);
                if (!Type.VOID_TYPE.equals(Type.getReturnType(descriptor))) {
                    stack.addLast(current);
                }
            } else if (current instanceof BtFieldInsn) {
                stack.addLast(current);
            } else if (current.isConstant()) {
                stack.addLast(current);
            }
            if (stack.size() == i) {
                return stack.getLast();
            }
        }
        return null;
    }

    private static int getMethodArgumentCount(String methodDescriptor) {
        return Type.getArgumentTypes(methodDescriptor).length;
    }
}
