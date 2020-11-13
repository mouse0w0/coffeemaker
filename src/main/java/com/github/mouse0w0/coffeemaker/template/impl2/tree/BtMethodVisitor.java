package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.insn.*;
import org.objectweb.asm.*;

class BtMethodVisitor extends MethodVisitor {
    private final BtMethod method;
    private final BtInsnList instructions;

    public BtMethodVisitor(int api, BtMethod method) {
        super(api);
        this.method = method;
        this.instructions = new BtInsnList();
        method.put(BtMethod.INSTRUCTIONS, instructions);
    }

    @Override
    public void visitParameter(String name, int access) {
        BtParameter parameter = new BtParameter(name, access);
        method.computeIfNull(BtMethod.PARAMETERS, k -> new BtList<>()).add(parameter);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return new BtAnnotationVisitor(api) {
            @Override
            public void putValue(String name, Object value) {
                method.putValue(BtMethod.ANNOTATION_DEFAULT, value);
            }
        };
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        BtAnnotation annotation = new BtAnnotation(descriptor, visible);
        method.getAnnotations().add(annotation);
        return new BtAnnotationVisitor(api, annotation);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        method.computeIfNull(BtMethod.ATTRIBUTES, k -> new BtList<>()).addValue(attribute);
    }

    @Override
    public void visitCode() {
        // Nothing to do.
    }

    @Override
    public void visitFrame(
            final int type,
            final int numLocal,
            final Object[] local,
            final int numStack,
            final Object[] stack) {
        instructions.add(new BtFrame(
                type,
                numLocal,
                local == null ? null : getBtLabels(local),
                numStack,
                stack == null ? null : getBtLabels(stack)));
    }

    @Override
    public void visitInsn(final int opcode) {
        instructions.add(new BtInsn(opcode));
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        instructions.add(new BtIntInsn(opcode, operand));
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        instructions.add(new BtVarInsn(opcode, var));
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        instructions.add(new BtTypeInsn(opcode, type));
    }

    @Override
    public void visitFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
        instructions.add(new BtFieldInsn(opcode, owner, name, descriptor));
    }

    @Override
    public void visitMethodInsn(
            final int opcodeAndSource,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
        if (api < Opcodes.ASM5 && (opcodeAndSource & Opcodes.SOURCE_DEPRECATED) == 0) {
            // Redirect the call to the deprecated version of this method.
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
            return;
        }
        int opcode = opcodeAndSource & ~Opcodes.SOURCE_MASK;

        instructions.add(new BtMethodInsn(opcode, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
        instructions.add(new BtInvokeDynamicInsn(
                name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        instructions.add(new BtJumpInsn(opcode, getBtLabel(label)));
    }

    @Override
    public void visitLabel(final Label label) {
        instructions.add(getBtLabel(label));
    }

    @Override
    public void visitLdcInsn(final Object value) {
        instructions.add(new BtLdcInsn(value));
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        instructions.add(new BtIincInsn(var, increment));
    }

    @Override
    public void visitTableSwitchInsn(
            final int min, final int max, final Label dflt, final Label... labels) {
        instructions.add(new BtTableSwitchInsn(min, max, getBtLabel(dflt), getBtLabels(labels)));
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        instructions.add(new BtLookupSwitchInsn(getBtLabel(dflt), keys, getBtLabels(labels)));
    }

    @Override
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        instructions.add(new BtMultiANewArrayInsn(descriptor, numDimensions));
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        method.computeIfNull(BtMethod.TRY_CATCH_BLOCKS, k -> new BtList<>())
                .add(new BtTryCatchBlock(getBtLabel(start), getBtLabel(end), getBtLabel(handler), type));
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        instructions.add(new BtLineNumber(line, getBtLabel(start)));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        method.putValue(BtMethod.MAX_STACK, maxStack);
        method.putValue(BtMethod.MAX_LOCALS, maxLocals);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    protected BtLabel getBtLabel(final Label label) {
        if (!(label.info instanceof BtLabel)) {
            label.info = new BtLabel();
        }
        return (BtLabel) label.info;
    }

    private BtLabel[] getBtLabels(final Label[] labels) {
        BtLabel[] labelNodes = new BtLabel[labels.length];
        for (int i = 0, n = labels.length; i < n; ++i) {
            labelNodes[i] = getBtLabel(labels[i]);
        }
        return labelNodes;
    }

    private Object[] getBtLabels(final Object[] objects) {
        Object[] labelNodes = new Object[objects.length];
        for (int i = 0, n = objects.length; i < n; ++i) {
            Object o = objects[i];
            if (o instanceof Label) {
                o = getBtLabel((Label) o);
            }
            labelNodes[i] = o;
        }
        return labelNodes;
    }
}
