package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.PrefixEvaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.BtLabel;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtForeach extends BtInsnNode {
    private final String iterable;
    private final String elementName;
    private final BtInsnList insnList;

    public BtForeach(String iterable, String elementName, BtInsnList insnList) {
        this.iterable = iterable;
        this.elementName = elementName;
        this.insnList = insnList;
    }

    @Override
    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        Iterable<?> iterable = evaluator.eval(this.iterable);
        for (Object value : iterable) {
            insnList.resetLabels();
            insnList.accept(methodVisitor, new PrefixEvaluator(evaluator, value, elementName));
        }
    }

    @Override
    public BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels) {
        return new BtForeach(iterable, elementName, insnList);
    }
}
