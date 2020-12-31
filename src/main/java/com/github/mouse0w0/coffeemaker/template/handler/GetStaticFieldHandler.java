package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class GetStaticFieldHandler extends MethodInsnHandler {

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$getStaticField", String.class)};
    }

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnNode arg0 = Utils.getPreviousConstant(insn, 0);
        BtInsnNode cast = insn.getNext();
        instructions.insert(insn, new BtComputableFieldInsn(Opcodes.GETSTATIC, arg0.getAsString()));
        instructions.remove(arg0);
        instructions.remove(insn);
        instructions.remove(cast);
    }
}
