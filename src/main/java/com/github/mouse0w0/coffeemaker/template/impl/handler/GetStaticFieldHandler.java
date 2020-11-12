package com.github.mouse0w0.coffeemaker.template.impl.handler;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.FieldInsn;
import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl.Processor;
import com.github.mouse0w0.coffeemaker.template.impl.Utils;
import com.github.mouse0w0.coffeemaker.template.impl.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.template.impl.extree.MethodNodeEx;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public class GetStaticFieldHandler implements InvokeMethodHandler {

    @Override
    public String[] getSupportedInvokeMethods() {
        return new String[]{Utils.getMethodId(Markers.class, "$staticField", String.class)};
    }

    @Override
    public void handle(MethodNodeEx method, MethodInsnNode insn, List<Processor> processors) {

    }

    private static class ProcessorImpl implements Processor {
        private final Method targetMethod;
        private final int targetInsn;
        private final String statement;

        public ProcessorImpl(Method targetMethod, int targetInsn, String statement) {
            this.targetMethod = targetMethod;
            this.targetInsn = targetInsn;
            this.statement = statement;
        }

        @Override
        public ClassNodeEx process(ClassNodeEx classNode, Evaluator evaluator) {
            Object value = evaluator.eval(statement);
            notNull(value);
            MethodNodeEx methodNode = classNode.methods.get(targetMethod);
            InsnList instructions = methodNode.instructions;
            AbstractInsnNode methodInsn = instructions.get(targetInsn);
            AbstractInsnNode insertPoint = methodInsn.getPrevious();
            instructions.remove(methodInsn.getNext());
            instructions.remove(methodInsn);
            instructions.set(insertPoint, ((FieldInsn) value).toInsnNode(Opcodes.GETSTATIC));
            return classNode;
        }

        private void notNull(Object value) {
            if (value == null) {
                throw new TemplateProcessException(String.format("eval(%s) cannot be null", statement));
            }
        }
    }
}
