package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.template.Constants;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl.ASMUtils;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

import static com.github.mouse0w0.coffeemaker.template.impl.ASMUtils.createInsnNode;

public class ConstantsHandler implements InvokeMethodHandler {

    public enum ValueType {
        INT, BOOL, CHAR, LONG, FLOAT, DOUBLE, STRING, CLASS;
    }

    @Override
    public String[] getSupportedInvokeMethods() {
        return new String[]{
                ASMUtils.getMethodId(Constants.class, "$int", String.class),
                ASMUtils.getMethodId(Constants.class, "$long", String.class),
                ASMUtils.getMethodId(Constants.class, "$float", String.class),
                ASMUtils.getMethodId(Constants.class, "$double", String.class),
                ASMUtils.getMethodId(Constants.class, "$string", String.class),
                ASMUtils.getMethodId(Constants.class, "$class", String.class),
                ASMUtils.getMethodId(Constants.class, "$bool", String.class),
                ASMUtils.getMethodId(Constants.class, "$byte", String.class),
                ASMUtils.getMethodId(Constants.class, "$short", String.class),
                ASMUtils.getMethodId(Constants.class, "$char", String.class),
        };
    }

    @Override
    public void handle(MethodNodeEx methodNode, MethodInsnNode insn, List<Processor> processors) {
        Method targetMethod = new Method(methodNode.name, methodNode.desc);
        int targetInsn = methodNode.instructions.indexOf(insn);
        String statement = (String) ((LdcInsnNode) insn.getPrevious()).cst;
        switch (insn.name) {
            case "$byte":
            case "$short":
            case "$int":
                processors.add(new ProcessorImpl(ValueType.INT, targetMethod, targetInsn, statement));
                break;
            case "$char":
                processors.add(new ProcessorImpl(ValueType.CHAR, targetMethod, targetInsn, statement));
                break;
            case "$bool":
                processors.add(new ProcessorImpl(ValueType.BOOL, targetMethod, targetInsn, statement));
                break;
            case "$long":
                processors.add(new ProcessorImpl(ValueType.LONG, targetMethod, targetInsn, statement));
                break;
            case "$float":
                processors.add(new ProcessorImpl(ValueType.FLOAT, targetMethod, targetInsn, statement));
                break;
            case "$double":
                processors.add(new ProcessorImpl(ValueType.DOUBLE, targetMethod, targetInsn, statement));
                break;
            case "$string":
                processors.add(new ProcessorImpl(ValueType.STRING, targetMethod, targetInsn, statement));
                break;
            case "$class":
                processors.add(new ProcessorImpl(ValueType.CLASS, targetMethod, targetInsn, statement));
                break;
        }
    }

    public static class ProcessorImpl implements Processor {
        private final ValueType valueType;
        private final Method targetMethod;
        private final int targetInsn;
        private final String statement;

        public ProcessorImpl(ValueType valueType, Method targetMethod, int targetInsn, String statement) {
            this.valueType = valueType;
            this.targetMethod = targetMethod;
            this.targetInsn = targetInsn;
            this.statement = statement;
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            Object value = evaluator.eval(statement);
            MethodNodeEx methodNode = classNode.methods.get(targetMethod);
            InsnList instructions = methodNode.instructions;
            AbstractInsnNode methodInsn = instructions.get(targetInsn);
            AbstractInsnNode insertPoint = methodInsn.getPrevious();
            instructions.remove(methodInsn);
            switch (valueType) {
                case INT:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode(((Number) value).intValue()));
                    return classNode;
                case CHAR:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode((Character) value));
                    return classNode;
                case BOOL:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode((Boolean) value));
                    return classNode;
                case LONG:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode((Long) value));
                    return classNode;
                case FLOAT:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode((Float) value));
                    return classNode;
                case DOUBLE:
                    notNull(value);
                    instructions.set(insertPoint, createInsnNode((Double) value));
                    return classNode;
                case STRING:
                    instructions.set(insertPoint, createInsnNode((String) value));
                    return classNode;
                case CLASS:
                    Type type = value instanceof Class<?> ? Type.getType((Class<?>) value) : (Type) value;
                    instructions.set(insertPoint, createInsnNode(type));
                    return classNode;
                default:
                    throw new TemplateProcessException("Should not reach here");
            }
        }

        private void notNull(Object value) {
            throw new TemplateProcessException(String.format("eval(%s) cannot be null", statement));
        }
    }
}
