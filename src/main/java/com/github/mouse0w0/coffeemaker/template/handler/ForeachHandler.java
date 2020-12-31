package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;

import java.lang.reflect.Method;

public class ForeachHandler extends MethodInsnHandler {
    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{
                Utils.getDeclaredMethod(Markers.class, "$foreach", String.class, String.class),
                Utils.getDeclaredMethod(Markers.class, "$endForeach")};
    }

    private BtInsnNode foreachInsn;

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        if ("$foreach".equals(insn.get(BtMethodInsn.NAME).getAsString())) {
            foreachInsn = insn;
        } else {
            BtInsnList instructions = method.getInstructions();
            BtInsnNode arg1 = Utils.getPreviousConstant(foreachInsn, 0);
            BtInsnNode arg0 = Utils.getPreviousConstant(foreachInsn, 1);
            BtInsnList insnList = Utils.subInsnList(instructions, foreachInsn.getNextLabel(), insn.getPreviousLabel());
            BtLabel injectPoint = insn.getNextLabel();
            Utils.removeRange(instructions, foreachInsn.getPreviousLabel(), injectPoint);
            instructions.insertBefore(injectPoint, new BtForeach(arg0.getAsString(), arg1.getAsString(), insnList));
        }
    }
}
