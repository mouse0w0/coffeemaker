package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnBase;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class GetStaticFieldHandler extends MethodInsnHandler {

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$getStaticField", String.class)};
    }

    @Override
    protected BtInsnNode handle(BtMethod method, BtMethodInsn insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnNode arg0 = Utils.getMethodArgument(insn, 0);
        BtInsnNode next = insn.getNext();
        instructions.insert(insn, new BtComputableFieldInsn(Opcodes.GETSTATIC, arg0.getAsString()));
        instructions.remove(arg0);
        instructions.remove(insn);
        if (next instanceof BtInsnBase && ((BtInsnBase) next).getOpcode() == Opcodes.CHECKCAST) {
            BtInsnNode cast = next;
            next = next.getNext();
            instructions.remove(cast);
        }
        return next;
    }
}
