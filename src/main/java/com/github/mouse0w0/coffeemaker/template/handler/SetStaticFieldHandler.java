package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;

public class SetStaticFieldHandler extends MethodInsnHandler {
    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{Utils.getDeclaredMethod(Markers.class, "$setStaticField", String.class, Object.class)};
    }

    @Override
    protected BtInsnNode handle(BtMethod method, BtMethodInsn insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnNode arg0 = Utils.getMethodArgument(insn, 0);
        BtComputableFieldInsn fieldInsn = new BtComputableFieldInsn(Opcodes.PUTSTATIC, arg0.getAsString());
        instructions.insert(insn, fieldInsn);
        instructions.remove(arg0);
        instructions.remove(insn);
        return fieldInsn;
    }
}
