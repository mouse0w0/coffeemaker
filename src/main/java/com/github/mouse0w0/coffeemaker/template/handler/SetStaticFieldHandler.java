package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLdcInsn;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class SetStaticFieldHandler extends MethodInsnHandler {
    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$setStaticField", String.class, Object.class)};
    }

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        BtInsnList instructions = method.getInstructions();
        BtLabel label = insn.getPreviousLabel();
        BtLdcInsn arg0 = (BtLdcInsn) label.getNext().getNext();
        instructions.insert(insn, new BtComputableFieldInsn(Opcodes.PUTSTATIC, arg0.getAsString()));
        instructions.remove(arg0);
        instructions.remove(insn);
    }
}
