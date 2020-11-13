package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtLabel;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtComputableFieldInsn extends BtInsnNode {

    private final int opcode;
    private final String expression;

    public BtComputableFieldInsn(int opcode, String expression) {
        this.opcode = opcode;
        this.expression = expression;
    }

    @Override
    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        Field field = evaluator.eval(expression);
        methodVisitor.visitFieldInsn(opcode, field.getOwner(), field.getName(), field.getDescriptor());
    }

    @Override
    public BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels) {
        return new BtComputableFieldInsn(opcode, expression);
    }
}
