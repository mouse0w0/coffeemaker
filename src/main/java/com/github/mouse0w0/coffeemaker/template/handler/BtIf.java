package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import com.github.mouse0w0.coffeemaker.template.util.Pair;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.Map;

public final class BtIf extends BtInsnNode {
    private final List<Pair<String, BtInsnList>> branches;
    private final BtInsnList elseBranch;

    public BtIf(List<Pair<String, BtInsnList>> branches, BtInsnList elseBranch) {
        this.branches = branches;
        this.elseBranch = elseBranch;
    }

    @Override
    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        for (Pair<String, BtInsnList> branch : branches) {
            if (evaluator.eval(branch.getLeft(), Boolean.class)) {
                BtInsnList insnList = branch.getRight();
                insnList.resetLabels();
                insnList.accept(methodVisitor, evaluator);
                return;
            }
        }

        if (elseBranch != null) {
            elseBranch.resetLabels();
            elseBranch.accept(methodVisitor, evaluator);
        }
    }

    @Override
    public BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels) {
        return new BtIf(branches, elseBranch);
    }
}
