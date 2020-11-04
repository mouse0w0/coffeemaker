package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.extree.MethodNodeEx;
import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.TemplateParseException;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import com.github.mouse0w0.coffeemaker.template.impl.Utils;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

import static com.github.mouse0w0.coffeemaker.template.impl.Utils.createInsnNode;

public class ConstantsHandler implements InvokeMethodHandler {

    public enum ValueType {
        INT, BOOL, CHAR, LONG, FLOAT, DOUBLE, STRING, CLASS;
    }

    @Override
    public String[] getSupportedInvokeMethods() {
        return new String[]{
                Utils.getMethodId(Markers.class, "$int", String.class),
                Utils.getMethodId(Markers.class, "$long", String.class),
                Utils.getMethodId(Markers.class, "$float", String.class),
                Utils.getMethodId(Markers.class, "$double", String.class),
                Utils.getMethodId(Markers.class, "$string", String.class),
                Utils.getMethodId(Markers.class, "$class", String.class),
                Utils.getMethodId(Markers.class, "$bool", String.class),
                Utils.getMethodId(Markers.class, "$byte", String.class),
                Utils.getMethodId(Markers.class, "$short", String.class),
                Utils.getMethodId(Markers.class, "$char", String.class),
        };
    }

    @Override
    public void handle(MethodNodeEx method, MethodInsnNode insn, List<Processor> processors) {
        Method targetMethod = new Method(method.name, method.desc);
        int targetInsn = method.instructions.indexOf(insn);
        AbstractInsnNode arg0 = insn.getPrevious();
        if (!(arg0 instanceof LdcInsnNode)) {
            throw new TemplateParseException(String.format("The parameter of %s method must be LDC", insn.name));
        }
        String statement = (String) ((LdcInsnNode) arg0).cst;
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
            if (value == null) {
                throw new TemplateProcessException(String.format("eval(%s) cannot be null", statement));
            }
        }
    }
}
