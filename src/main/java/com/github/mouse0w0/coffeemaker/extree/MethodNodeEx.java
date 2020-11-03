package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.*;

public class MethodNodeEx extends MethodVisitor {

    /**
     * The method's access flags (see {@link Opcodes}). This field also indicates if the method is
     * synthetic and/or deprecated.
     */
    public int access;

    /**
     * The method's name.
     */
    public String name;

    /**
     * The method's descriptor (see {@link Type}).
     */
    public String desc;

    /**
     * The method's signature. May be {@literal null}.
     */
    public String signature;

    /**
     * The internal names of the method's exception classes (see {@link Type#getInternalName()}).
     */
    public List<String> exceptions;

    public List<ParameterNodeEx> parameters;

    public Map<String, AnnotationNodeEx> annotations;

    public Map<String, TypeAnnotationNodeEx> typeAnnotations;

    /**
     * The non standard attributes of this method. May be {@literal null}.
     */
    public List<Attribute> attrs;

    /**
     * The default value of this annotation interface method. This field must be a {@link Byte},
     * {@link Boolean}, {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link
     * Float}, {@link Double}, {@link String} or {@link Type}, or an two elements String array (for
     * enumeration values), a {@link AnnotationNode}, or a {@link List} of values of one of the
     * preceding types. May be {@literal null}.
     */
    public Object annotationDefault;

    /**
     * The instructions of this method.
     */
    public InsnList instructions;

    /**
     * The try catch blocks of this method.
     */
    public List<TryCatchBlockNode> tryCatchBlocks;

    /**
     * The maximum stack size of this method.
     */
    public int maxStack;

    /**
     * The maximum number of local variables of this method.
     */
    public int maxLocals;

    /**
     * The local variables of this method. May be {@literal null}
     */
    public List<LocalVariableNode> localVariables;

    /**
     * The visible local variable annotations of this method. May be {@literal null}
     */
    public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;

    /**
     * The invisible local variable annotations of this method. May be {@literal null}
     */
    public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;

    /**
     * Whether the accept method has been called on this object.
     */
    private boolean visited;

    public MethodNodeEx() {
        this(/* latest api = */ Opcodes.ASM8);
    }

    public MethodNodeEx(final int api) {
        super(api);
        this.instructions = new InsnList();
    }

    public MethodNodeEx(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        this(/* latest api = */ Opcodes.ASM8, access, name, descriptor, signature, exceptions);
    }

    public MethodNodeEx(
            final int api,
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        super(api);
        this.access = access;
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
        this.exceptions = Util.asArrayList(exceptions);
        if ((access & Opcodes.ACC_ABSTRACT) == 0) {
            this.localVariables = new ArrayList<>(5);
        }
        this.tryCatchBlocks = new ArrayList<>();
        this.instructions = new InsnList();
    }

    public Collection<AnnotationNodeEx> getAnnotations() {
        return annotations == null ? Collections.emptyList() : annotations.values();
    }

    public AnnotationNodeEx getAnnotation(String descriptor) {
        return annotations == null ? null : annotations.get(descriptor);
    }

    public void addAnnotation(AnnotationNodeEx annotationNode) {
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(annotationNode.desc, annotationNode);
    }

    public TypeAnnotationNodeEx getTypeAnnotation(String descriptor) {
        return typeAnnotations == null ? null : typeAnnotations.get(descriptor);
    }

    public void addTypeAnnotation(TypeAnnotationNodeEx typeAnnotation) {
        if (typeAnnotations == null) {
            typeAnnotations = new LinkedHashMap<>(2);
        }
        typeAnnotations.put(typeAnnotation.desc, typeAnnotation);
    }

    public ParameterNodeEx getParameter(int index) {
        return parameters.get(index);
    }

    public ParameterNodeEx getParameter(String name) {
        if (parameters == null) return null;
        for (ParameterNodeEx parameter : parameters) {
            if (parameter.name.equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    public void addParameter(ParameterNodeEx parameter) {
        if (parameters == null) {
            parameters = new ArrayList<>(5);
        }
        parameters.add(parameter);
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the MethodVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public void visitParameter(final String name, final int access) {
        addParameter(new ParameterNodeEx(name, access));
    }

    @Override
    @SuppressWarnings("serial")
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNodeEx(
                new ArrayList<Object>(0) {
                    @Override
                    public boolean add(final Object o) {
                        annotationDefault = o;
                        return super.add(o);
                    }
                });
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor, visible);
        addAnnotation(annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        TypeAnnotationNodeEx typeAnnotation = new TypeAnnotationNodeEx(typeRef, typePath, descriptor, visible);
        addTypeAnnotation(typeAnnotation);
        return typeAnnotation;
    }

    @Override
    public void visitAnnotableParameterCount(final int parameterCount, final boolean visible) {
        // Nothing to do.
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnnotationVisitor visitParameterAnnotation(
            final int parameter, final String descriptor, final boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor, visible);
        getParameter(parameter).addAnnotation(annotation);
        return annotation;
    }

    @Override
    public void visitAttribute(final Attribute attribute) {
        attrs = Util.add(attrs, attribute);
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
        instructions.add(
                new FrameNode(
                        type,
                        numLocal,
                        local == null ? null : getLabelNodes(local),
                        numStack,
                        stack == null ? null : getLabelNodes(stack)));
    }

    @Override
    public void visitInsn(final int opcode) {
        instructions.add(new InsnNode(opcode));
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        instructions.add(new IntInsnNode(opcode, operand));
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        instructions.add(new VarInsnNode(opcode, var));
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        instructions.add(new TypeInsnNode(opcode, type));
    }

    @Override
    public void visitFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
        instructions.add(new FieldInsnNode(opcode, owner, name, descriptor));
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

        instructions.add(new MethodInsnNode(opcode, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
        instructions.add(
                new InvokeDynamicInsnNode(
                        name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        instructions.add(new JumpInsnNode(opcode, getLabelNode(label)));
    }

    @Override
    public void visitLabel(final Label label) {
        instructions.add(getLabelNode(label));
    }

    @Override
    public void visitLdcInsn(final Object value) {
        instructions.add(new LdcInsnNode(value));
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        instructions.add(new IincInsnNode(var, increment));
    }

    @Override
    public void visitTableSwitchInsn(
            final int min, final int max, final Label dflt, final Label... labels) {
        instructions.add(new TableSwitchInsnNode(min, max, getLabelNode(dflt), getLabelNodes(labels)));
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        instructions.add(new LookupSwitchInsnNode(getLabelNode(dflt), keys, getLabelNodes(labels)));
    }

    @Override
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        instructions.add(new MultiANewArrayInsnNode(descriptor, numDimensions));
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        // Find the last real instruction, i.e. the instruction targeted by this annotation.
        AbstractInsnNode currentInsn = instructions.getLast();
        while (currentInsn.getOpcode() == -1) {
            currentInsn = currentInsn.getPrevious();
        }
        // Add the annotation to this instruction.
        TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor);
        if (visible) {
            currentInsn.visibleTypeAnnotations =
                    Util.add(currentInsn.visibleTypeAnnotations, typeAnnotation);
        } else {
            currentInsn.invisibleTypeAnnotations =
                    Util.add(currentInsn.invisibleTypeAnnotations, typeAnnotation);
        }
        return typeAnnotation;
    }

    @Override
    public void visitTryCatchBlock(
            final Label start, final Label end, final Label handler, final String type) {
        TryCatchBlockNode tryCatchBlock =
                new TryCatchBlockNode(getLabelNode(start), getLabelNode(end), getLabelNode(handler), type);
        tryCatchBlocks = Util.add(tryCatchBlocks, tryCatchBlock);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        TryCatchBlockNode tryCatchBlock = tryCatchBlocks.get((typeRef & 0x00FFFF00) >> 8);
        TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor);
        if (visible) {
            tryCatchBlock.visibleTypeAnnotations =
                    Util.add(tryCatchBlock.visibleTypeAnnotations, typeAnnotation);
        } else {
            tryCatchBlock.invisibleTypeAnnotations =
                    Util.add(tryCatchBlock.invisibleTypeAnnotations, typeAnnotation);
        }
        return typeAnnotation;
    }

    @Override
    public void visitLocalVariable(
            final String name,
            final String descriptor,
            final String signature,
            final Label start,
            final Label end,
            final int index) {
        LocalVariableNode localVariable =
                new LocalVariableNode(
                        name, descriptor, signature, getLabelNode(start), getLabelNode(end), index);
        localVariables = Util.add(localVariables, localVariable);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(
            final int typeRef,
            final TypePath typePath,
            final Label[] start,
            final Label[] end,
            final int[] index,
            final String descriptor,
            final boolean visible) {
        LocalVariableAnnotationNode localVariableAnnotation =
                new LocalVariableAnnotationNode(
                        typeRef, typePath, getLabelNodes(start), getLabelNodes(end), index, descriptor);
        if (visible) {
            visibleLocalVariableAnnotations =
                    Util.add(visibleLocalVariableAnnotations, localVariableAnnotation);
        } else {
            invisibleLocalVariableAnnotations =
                    Util.add(invisibleLocalVariableAnnotations, localVariableAnnotation);
        }
        return localVariableAnnotation;
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        instructions.add(new LineNumberNode(line, getLabelNode(start)));
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    /**
     * Returns the LabelNode corresponding to the given Label. Creates a new LabelNode if necessary.
     * The default implementation of this method uses the {@link Label#info} field to store
     * associations between labels and label nodes.
     *
     * @param label a Label.
     * @return the LabelNode corresponding to label.
     */
    protected LabelNode getLabelNode(final Label label) {
        if (!(label.info instanceof LabelNode)) {
            label.info = new LabelNode();
        }
        return (LabelNode) label.info;
    }

    private LabelNode[] getLabelNodes(final Label[] labels) {
        LabelNode[] labelNodes = new LabelNode[labels.length];
        for (int i = 0, n = labels.length; i < n; ++i) {
            labelNodes[i] = getLabelNode(labels[i]);
        }
        return labelNodes;
    }

    private Object[] getLabelNodes(final Object[] objects) {
        Object[] labelNodes = new Object[objects.length];
        for (int i = 0, n = objects.length; i < n; ++i) {
            Object o = objects[i];
            if (o instanceof Label) {
                o = getLabelNode((Label) o);
            }
            labelNodes[i] = o;
        }
        return labelNodes;
    }

    // -----------------------------------------------------------------------------------------------
    // Accept method
    // -----------------------------------------------------------------------------------------------

    /**
     * Checks that this method node is compatible with the given ASM API version. This method checks
     * that this node, and all its children recursively, do not contain elements that were introduced
     * in more recent versions of the ASM API than the given version.
     *
     * @param api an ASM API version. Must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5},
     *            {@link Opcodes#ASM6}, {@link Opcodes#ASM7} or {@link Opcodes#ASM8}.
     */
    public void check(final int api) {
        if (api == Opcodes.ASM4) {
            if (parameters != null && !parameters.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
            if (typeAnnotations != null && !typeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
            if (tryCatchBlocks != null) {
                for (int i = tryCatchBlocks.size() - 1; i >= 0; --i) {
                    TryCatchBlockNode tryCatchBlock = tryCatchBlocks.get(i);
                    if (tryCatchBlock.visibleTypeAnnotations != null
                            && !tryCatchBlock.visibleTypeAnnotations.isEmpty()) {
                        throw new UnsupportedClassVersionException();
                    }
                    if (tryCatchBlock.invisibleTypeAnnotations != null
                            && !tryCatchBlock.invisibleTypeAnnotations.isEmpty()) {
                        throw new UnsupportedClassVersionException();
                    }
                }
            }
            for (int i = instructions.size() - 1; i >= 0; --i) {
                AbstractInsnNode insn = instructions.get(i);
                if (insn.visibleTypeAnnotations != null && !insn.visibleTypeAnnotations.isEmpty()) {
                    throw new UnsupportedClassVersionException();
                }
                if (insn.invisibleTypeAnnotations != null && !insn.invisibleTypeAnnotations.isEmpty()) {
                    throw new UnsupportedClassVersionException();
                }
                if (insn instanceof MethodInsnNode) {
                    boolean isInterface = ((MethodInsnNode) insn).itf;
                    if (isInterface != (insn.getOpcode() == Opcodes.INVOKEINTERFACE)) {
                        throw new UnsupportedClassVersionException();
                    }
                } else if (insn instanceof LdcInsnNode) {
                    Object value = ((LdcInsnNode) insn).cst;
                    if (value instanceof Handle
                            || (value instanceof Type && ((Type) value).getSort() == Type.METHOD)) {
                        throw new UnsupportedClassVersionException();
                    }
                }
            }
            if (visibleLocalVariableAnnotations != null && !visibleLocalVariableAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
            if (invisibleLocalVariableAnnotations != null
                    && !invisibleLocalVariableAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
        if (api < Opcodes.ASM7) {
            for (int i = instructions.size() - 1; i >= 0; --i) {
                AbstractInsnNode insn = instructions.get(i);
                if (insn instanceof LdcInsnNode) {
                    Object value = ((LdcInsnNode) insn).cst;
                    if (value instanceof ConstantDynamic) {
                        throw new UnsupportedClassVersionException();
                    }
                }
            }
        }
    }

    /**
     * Makes the given class visitor visit this method.
     *
     * @param classVisitor a class visitor.
     */
    public void accept(final ClassVisitor classVisitor) {
        String[] exceptionsArray = exceptions == null ? null : exceptions.toArray(new String[0]);
        MethodVisitor methodVisitor =
                classVisitor.visitMethod(access, name, desc, signature, exceptionsArray);
        if (methodVisitor != null) {
            accept(methodVisitor);
        }
    }

    /**
     * Makes the given method visitor visit this method.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor) {
        // Visit the parameters.
        if (parameters != null) {
            for (int i = 0, n = parameters.size(); i < n; i++) {
                parameters.get(i).accept(methodVisitor);
            }
        }
        // Visit the annotations.
        if (annotationDefault != null) {
            AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
            AnnotationNodeEx.accept(annotationVisitor, null, annotationDefault);
            if (annotationVisitor != null) {
                annotationVisitor.visitEnd();
            }
        }
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.accept(methodVisitor.visitAnnotation(annotation.desc, annotation.visible));
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.accept(
                        methodVisitor.visitTypeAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
            }
        }

        int visibleAnnotableParameterCount = 0;
        int invisibleAnnotableParameterCount = 0;
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                ParameterNodeEx parameter = parameters.get(i);
                if (parameter.annotations == null) continue;

                for (AnnotationNodeEx annotation : parameter.annotations.values()) {
                    if (annotation.visible) {
                        visibleAnnotableParameterCount = i + 1;
                    } else {
                        invisibleAnnotableParameterCount = i + 1;
                    }
                }
            }
        }

        if (visibleAnnotableParameterCount > 0) {
            methodVisitor.visitAnnotableParameterCount(visibleAnnotableParameterCount, true);
            for (int i = 0; i < visibleAnnotableParameterCount; i++) {
                parameters.get(i).acceptParameterAnnotation(methodVisitor, i, true);
            }
        }
        if (invisibleAnnotableParameterCount > 0) {
            methodVisitor.visitAnnotableParameterCount(invisibleAnnotableParameterCount, false);
            for (int i = 0; i < visibleAnnotableParameterCount; i++) {
                parameters.get(i).acceptParameterAnnotation(methodVisitor, i, false);
            }
        }
        // Visit the non standard attributes.
        if (visited) {
            instructions.resetLabels();
        }
        if (attrs != null) {
            for (int i = 0, n = attrs.size(); i < n; ++i) {
                methodVisitor.visitAttribute(attrs.get(i));
            }
        }
        // Visit the code.
        if (instructions.size() > 0) {
            methodVisitor.visitCode();
            // Visits the try catch blocks.
            if (tryCatchBlocks != null) {
                for (int i = 0, n = tryCatchBlocks.size(); i < n; ++i) {
                    tryCatchBlocks.get(i).updateIndex(i);
                    tryCatchBlocks.get(i).accept(methodVisitor);
                }
            }
            // Visit the instructions.
            instructions.accept(methodVisitor);
            // Visits the local variables.
            if (localVariables != null) {
                for (int i = 0, n = localVariables.size(); i < n; ++i) {
                    localVariables.get(i).accept(methodVisitor);
                }
            }
            // Visits the local variable annotations.
            if (visibleLocalVariableAnnotations != null) {
                for (int i = 0, n = visibleLocalVariableAnnotations.size(); i < n; ++i) {
                    visibleLocalVariableAnnotations.get(i).accept(methodVisitor, true);
                }
            }
            if (invisibleLocalVariableAnnotations != null) {
                for (int i = 0, n = invisibleLocalVariableAnnotations.size(); i < n; ++i) {
                    invisibleLocalVariableAnnotations.get(i).accept(methodVisitor, false);
                }
            }
            methodVisitor.visitMaxs(maxStack, maxLocals);
            visited = true;
        }
        methodVisitor.visitEnd();
    }
}
