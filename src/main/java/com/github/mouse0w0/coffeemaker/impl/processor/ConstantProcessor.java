package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.asm.ASMUtils;
import com.github.mouse0w0.coffeemaker.asm.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.asm.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.impl.handler.InvokeMethodHandler;
import com.github.mouse0w0.coffeemaker.syntax.Constants;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public abstract class ConstantProcessor implements Processor {

    private final String methodId;
    private final int insnIndex;
    protected final String statement;

    public static InvokeMethodHandler handler() {
        return new Handler();
    }

    public ConstantProcessor(String methodId, int insnIndex, String statement) {
        this.methodId = methodId;
        this.insnIndex = insnIndex;
        this.statement = statement;
    }

    protected abstract void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator);

    @Override
    public void process(ClassNodeEx classNode, Evaluator evaluator) {
        MethodNodeEx method = classNode.getMethodEx(methodId);
        InsnList instructions = method.instructions;
        AbstractInsnNode methodInsn = instructions.get(insnIndex);
        AbstractInsnNode insertPoint = methodInsn.getPrevious();
        instructions.remove(methodInsn);
        process(instructions, insertPoint, evaluator);
    }

    private static class LdcProcessor extends ConstantProcessor {

        public LdcProcessor(String methodId, int insnIndex, String statement) {
            super(methodId, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            instructions.set(insertPoint, new LdcInsnNode(evaluator.eval(statement)));
        }
    }

    private static class BooleanProcessor extends ConstantProcessor {

        public BooleanProcessor(String methodId, int insnIndex, String statement) {
            super(methodId, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            boolean value = evaluator.eval(statement);
            instructions.set(insertPoint, new InsnNode(value ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
        }
    }

    private static class ByteProcessor extends ConstantProcessor {

        public ByteProcessor(String methodId, int insnIndex, String statement) {
            super(methodId, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            Number number = evaluator.eval(statement);
            instructions.set(insertPoint, new IntInsnNode(Opcodes.BIPUSH, number.byteValue()));
        }
    }

    private static class ShortProcessor extends ConstantProcessor {

        public ShortProcessor(String methodId, int insnIndex, String statement) {
            super(methodId, insnIndex, statement);
        }

        @Override
        protected void process(InsnList instructions, AbstractInsnNode insertPoint, Evaluator evaluator) {
            Number number = evaluator.eval(statement);
            instructions.set(insertPoint, new IntInsnNode(Opcodes.SIPUSH, number.shortValue()));
        }
    }

    private static class CharProcessor extends ConstantProcessor {

        public CharProcessor(String methodId, int insnIndex, String statement) {
            super(methodId, insnIndex, statement);
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
        public void handle(MethodNodeEx method, MethodInsnNode insn, List<Processor> processors) {
            String methodId = ASMUtils.getMethodId(method);
            int insnIndex = method.instructions.indexOf(insn);
            LdcInsnNode statementLdcInsn = (LdcInsnNode) insn.getPrevious();
            String statement = (String) statementLdcInsn.cst;
            switch (insn.name) {
                case "$int":
                case "$long":
                case "$float":
                case "$double":
                case "$string":
                case "$class":
                    processors.add(new LdcProcessor(methodId, insnIndex, statement));
                    break;
                case "$boolean":
                    processors.add(new BooleanProcessor(methodId, insnIndex, statement));
                    break;
                case "$byte":
                    processors.add(new ByteProcessor(methodId, insnIndex, statement));
                    break;
                case "$short":
                    processors.add(new ShortProcessor(methodId, insnIndex, statement));
                    break;
                case "$char":
                    processors.add(new CharProcessor(methodId, insnIndex, statement));
                    break;
            }
        }
    }
}
