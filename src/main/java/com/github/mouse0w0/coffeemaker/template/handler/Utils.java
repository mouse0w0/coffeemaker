package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;

import java.lang.reflect.Method;

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

    public static void removeLabel(BtInsnList insnList, BtInsnNode node) {
        BtInsnNode start = findPreviousLabel(node);
        BtInsnNode end = findNextLabel(node);
        removeRange(insnList, start, end);
    }

    public static BtLabel findPreviousLabel(BtInsnNode node) {
        BtInsnNode current = node;
        while (!(current instanceof BtLabel) && current != null) {
            current = current.getPrevious();
        }
        return (BtLabel) current;
    }

    public static BtLabel findNextLabel(BtInsnNode node) {
        BtInsnNode current = node.getNext();
        while (!(current instanceof BtLabel) && current != null) {
            current = current.getNext();
        }
        return (BtLabel) current;
    }
}
