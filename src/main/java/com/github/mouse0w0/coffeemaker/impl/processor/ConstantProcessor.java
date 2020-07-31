package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.asm.extree.ClassNodeEx;
import com.github.mouse0w0.asm.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.impl.handler.InvokeMethodHandler;
import com.github.mouse0w0.coffeemaker.syntax.Constants;
import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.*;

import java.util.List;

public abstract class ConstantProcessor implements Processor {

    private final Method method;
    private final int insnIndex;
    protected final String statement;

    public static InvokeMethodHandler handler() {
        return new Handler();
    }

    public ConstantProcessor(Method method, int insnIndex, String statement) {
        this.method = method;
        this.insnIndex = insnIndex;
        this.statement = statement;
    }

    protected abstract void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator);

    @Override
    public void process(ClassNodeEx classNode, Evaluator evaluator) {
        MethodNodeEx methodNode = classNode.methods.get(method);
        InsnList instructions = methodNode.instructions;
        AbstractInsnNode methodInsn = instructions.get(insnIndex);
        AbstractInsnNode insertPoint = methodInsn.getPrevious();
        instructions.remove(methodInsn);
        process(instructions, insertPoint, evaluator);
    }

    private static class LdcProcessor extends ConstantProcessor {

        public LdcProcessor(Method method, int insnIndex, String statement) {
            super(method, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            instructions.set(insertPoint, new LdcInsnNode(evaluator.eval(statement)));
        }
    }

    private static class BooleanProcessor extends ConstantProcessor {

        public BooleanProcessor(Method method, int insnIndex, String statement) {
            super(method, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            boolean value = evaluator.eval(statement);
            instructions.set(insertPoint, new InsnNode(value ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
        }
    }

    private static class ByteProcessor extends ConstantProcessor {

        public ByteProcessor(Method method, int insnIndex, String statement) {
            super(method, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            Number number = evaluator.eval(statement);
            instructions.set(insertPoint, new IntInsnNode(Opcodes.BIPUSH, number.byteValue()));
        }
    }

    private static class ShortProcessor extends ConstantProcessor {

        public ShortProcessor(Method method, int insnIndex, String statement) {
            super(method, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            Number number = evaluator.eval(statement);
            instructions.set(insertPoint, new IntInsnNode(Opcodes.SIPUSH, number.shortValue()));
        }
    }

    private static class CharProcessor extends ConstantProcessor {

        public CharProcessor(Method method, int insnIndex, String statement) {
            super(method, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            char c = evaluator.eval(statement);
            if (c <= Byte.MAX_VALUE) {
                instructions.set(insertPoint, new IntInsnNode(Opcodes.BIPUSH, c));
            } else if (c <= Short.MAX_VALUE) {
                instructions.set(insertPoint, new IntInsnNode(Opcodes.SIPUSH, c));
            } else {
                instructions.set(insertPoint, new LdcInsnNode(Integer.valueOf(c)));
            }
        }
    }

    private static class Handler implements InvokeMethodHandler {

        @Override
        public String[] getSupportedInvokeMethods() {
            return new String[]{
                    ASMUtils.getMethodId(Constants.class, "$int", String.class),
                    ASMUtils.getMethodId(Constants.class, "$long", String.class),
                    ASMUtils.getMethodId(Constants.class, "$float", String.class),
                    ASMUtils.getMethodId(Constants.class, "$double", String.class),
                    ASMUtils.getMethodId(Constants.class, "$string", String.class),
                    ASMUtils.getMethodId(Constants.class, "$class", String.class),
                    ASMUtils.getMethodId(Constants.class, "$boolean", String.class),
                    ASMUtils.getMethodId(Constants.class, "$byte", String.class),
                    ASMUtils.getMethodId(Constants.class, "$short", String.class),
                    ASMUtils.getMethodId(Constants.class, "$char", String.class),
            };
        }

        @Override
        public void handle(MethodNodeEx methodNode, MethodInsnNode insn, List<Processor> processors) {
            Method method = new Method(methodNode.name, methodNode.desc);
            int insnIndex = methodNode.instructions.indexOf(insn);
            LdcInsnNode statementLdcInsn = (LdcInsnNode) insn.getPrevious();
            String statement = (String) statementLdcInsn.cst;
            switch (insn.name) {
                case "$int":
                case "$long":
                case "$float":
                case "$double":
                case "$string":
                case "$class":
                    processors.add(new LdcProcessor(method, insnIndex, statement));
                    break;
                case "$boolean":
                    processors.add(new BooleanProcessor(method, insnIndex, statement));
                    break;
                case "$byte":
                    processors.add(new ByteProcessor(method, insnIndex, statement));
                    break;
                case "$short":
                    processors.add(new ShortProcessor(method, insnIndex, statement));
                    break;
                case "$char":
                    processors.add(new CharProcessor(method, insnIndex, statement));
                    break;
            }
        }
    }
}
