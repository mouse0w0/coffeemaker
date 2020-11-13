package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtLabel;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtComputableConstant extends BtInsnNode {

    private final ConstantType type;
    private final String expression;

    public BtComputableConstant(ConstantType type, String expression) {
        this.type = type;
        this.expression = expression;
    }

    @Override
    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        type.accept(methodVisitor, evaluator.eval(expression));
    }

    @Override
    public BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels) {
        return new BtComputableConstant(type, expression);
    }
}
