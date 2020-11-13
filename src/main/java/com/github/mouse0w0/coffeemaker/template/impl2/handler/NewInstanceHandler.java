package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtValue;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.*;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class NewInstanceHandler extends MethodInsnHandler {

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$new", String.class)};
    }

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        BtInsnList instructions = method.getInstructions();
        BtLdcInsn arg0 = (BtLdcInsn) insn.getPrevious();
        BtTypeInsn newType = new BtTypeInsn(Opcodes.NEW, new BtComputableValue(arg0.getAsString()));
        BtInsn dup = new BtInsn(Opcodes.DUP);
        BtMethodInsn invokeInit = new BtMethodInsn(Opcodes.INVOKESPECIAL, new BtComputableValue(arg0.getAsString()),
                new BtValue("<init>"), new BtValue("()V"), new BtValue(false));
        instructions.insert(insn, newType);
        instructions.insert(newType, dup);
        instructions.insert(dup, invokeInit);
        instructions.remove(arg0);
        instructions.remove(insn);
    }
}
