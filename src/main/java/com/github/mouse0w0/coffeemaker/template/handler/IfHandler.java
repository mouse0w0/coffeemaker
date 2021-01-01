package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.TemplateParseException;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;
import com.github.mouse0w0.coffeemaker.template.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IfHandler extends MethodInsnHandler {

    private enum State {
        BRANCH,
        ELSE,
        END
    }

    private State state = State.END;
    private BtMethodInsn startInsnNode;
    private BtMethodInsn previousInsnNode;
    private List<Pair<String, BtInsnList>> branches;

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{
                Utils.getDeclaredMethod(Markers.class, "$if", String.class),
                Utils.getDeclaredMethod(Markers.class, "$elseIf", String.class),
                Utils.getDeclaredMethod(Markers.class, "$else"),
                Utils.getDeclaredMethod(Markers.class, "$endIf")};
    }

    @Override
    protected void handle(BtMethod method, BtMethodInsn insn) {
        String methodName = insn.get(BtMethodInsn.NAME).getAsString();
        if ("$if".equals(methodName)) {
            if (state != State.END) throw new TemplateParseException("open if");
            state = State.BRANCH;
            startInsnNode = previousInsnNode = insn;
            branches = new ArrayList<>();
        } else if ("$elseIf".equals(methodName)) {
            if (state == State.END) throw new TemplateParseException("isolated else jf");
            if (state == State.ELSE) throw new TemplateParseException("else branch reached");
            branch(method, insn);
            previousInsnNode = insn;
        } else if ("$else".equals(methodName)) {
            if (state == State.END) throw new TemplateParseException("isolated else");
            if (state == State.ELSE) throw new TemplateParseException("too much else branch");
            branch(method, insn);
            state = State.ELSE;
            previousInsnNode = insn;
        } else if ("$endIf".equals(methodName)) {
            if (state == State.END) throw new TemplateParseException("isolated end if");
            endIf(method, insn);
            state = State.END;
            startInsnNode = previousInsnNode = null;
            branches = null;
        }
    }

    private void branch(BtMethod method, BtMethodInsn insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnNode expression = Utils.getMethodArgument(previousInsnNode, 0);
        BtInsnList branch = Utils.subInsnList(instructions,
                previousInsnNode.getNextLabel(),
                insn.getPreviousLabel());
        branches.add(Pair.of(expression.getAsString(), branch));
    }

    private void endIf(BtMethod method, BtMethodInsn insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnList elseBranch = null;
        if (state == State.BRANCH) branch(method, insn);
        else elseBranch = Utils.subInsnList(instructions,
                previousInsnNode.getNextLabel(), insn.getPreviousLabel());

        BtLabel label = insn.getNextLabel();
        Utils.removeRange(instructions, startInsnNode.getPreviousLabel(), label);
        instructions.insertBefore(label, new BtIf(branches, elseBranch));
    }
}
