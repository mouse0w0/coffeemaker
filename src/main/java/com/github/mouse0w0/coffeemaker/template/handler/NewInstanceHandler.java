package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.BtValue;
import com.github.mouse0w0.coffeemaker.template.tree.insn.*;
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
        BtInsnNode arg0 = Utils.getPreviousConstant(insn, 0);
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
