package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtLdcInsn;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class GetStaticFieldHandler extends MethodInsnHandler {

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$staticField", String.class)};
    }

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        BtInsnList instructions = method.getInstructions();
        BtLdcInsn arg0 = (BtLdcInsn) insn.getPrevious();
        instructions.insert(insn, new BtComputableFieldInsn(Opcodes.GETSTATIC, arg0.getAsString()));
        instructions.remove(arg0);
        instructions.remove(insn);
    }
}
