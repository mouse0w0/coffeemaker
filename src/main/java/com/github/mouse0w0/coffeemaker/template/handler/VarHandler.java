package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtFieldInsn;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;

import java.lang.reflect.Method;

public class VarHandler extends MethodInsnHandler {
    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{
                Utils.getDeclaredMethod(Markers.class, "$classVar", String.class, Class.class),
                Utils.getDeclaredMethod(Markers.class, "$staticFieldVar", String.class, Object.class)};
    }

    @Override
    protected BtInsnNode handle(BtMethod method, BtMethodInsn insn) {
        String name = insn.get(BtMethodInsn.NAME).getAsString();
        BtInsnNode value = Utils.getMethodArgument(insn, 1);
        BtInsnNode key = Utils.getMethodArgument(insn, 0);
        BtClass clazz = (BtClass) method.getParent().getParent();
        if ("$classVar".equals(name)) {
            clazz.getLocalVar().put(key.getAsString(), value.getAsType());
        } else if ("$staticFieldVar".equals(name)) {
            clazz.getLocalVar().put(key.getAsString(), new Field((BtFieldInsn) value));
        }
        BtLabel next = insn.getNextLabel();
        Utils.removeRange(method.getInstructions(), insn.getPreviousLabel(), next);
        return next;
    }
}
