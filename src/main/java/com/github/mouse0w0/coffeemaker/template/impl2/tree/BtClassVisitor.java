package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;
import org.objectweb.asm.*;

public class BtClassVisitor extends ClassVisitor {
    private BtClass clazz;

    public BtClassVisitor() {
        super(Opcodes.ASM5);
    }

    public BtClass getBtClass() {
        return clazz;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        clazz = new BtClass();
        clazz.putValue(BtClass.VERSION, version);
        clazz.putValue(BtClass.ACCESS, access);
        clazz.putValue(BtClass.NAME, name);
        clazz.putValue(BtClass.SIGNATURE, signature);
        clazz.putValue(BtClass.SUPER_NAME, superName);
        clazz.putValue(BtClass.INTERFACES, new SmartList<>(interfaces));
    }

    @Override
    public void visitSource(String source, String debug) {
        clazz.putValue(BtClass.SOURCE_FILE, source);
        clazz.putValue(BtClass.SOURCE_DEBUG, debug);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitNestHost(String nestHost) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        BtAnnotation annotation = new BtAnnotation(descriptor, visible);
        clazz.getAnnotations().add(annotation);
        return new BtAnnotationVisitor(api, annotation);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        clazz.computeIfNull(BtClass.ATTRIBUTES, k -> new BtList<>())
                .addValue(attribute);
    }

    @Override
    public void visitNestMember(String nestMember) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        clazz.computeIfNull(BtClass.INNER_CLASSES, k -> new BtList<>())
                .add(new BtInnerClass(name, outerName, innerName, access));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        BtField field = new BtField(access, name, descriptor, signature, value);
        clazz.getFields().add(field);
        return new BtFieldVisitor(api, field);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        BtMethod method = new BtMethod(access, name, descriptor, signature, exceptions);
        clazz.getMethods().add(method);
        return new BtMethodVisitor(api, method);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }
}
