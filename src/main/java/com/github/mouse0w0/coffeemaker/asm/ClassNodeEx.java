package com.github.mouse0w0.coffeemaker.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public class ClassNodeEx extends ClassNode {

    private final Map<String, AnnotationNodeEx> annotationsEx = new HashMap<>();
    private final Map<String, FieldNodeEx> fieldsEx = new HashMap<>();
    private final Map<MethodIdentifier, MethodNodeEx> methodsEx = new HashMap<>();

    public Map<String, AnnotationNodeEx> getAnnotationsEx() {
        return annotationsEx;
    }

    public AnnotationNodeEx getAnnotationEx(String descriptor) {
        return annotationsEx.get(descriptor);
    }

    public Map<String, FieldNodeEx> getFieldsEx() {
        return fieldsEx;
    }

    public FieldNodeEx getFieldEx(String name) {
        return fieldsEx.get(name);
    }

    public Map<MethodIdentifier, MethodNodeEx> getMethodsEx() {
        return methodsEx;
    }

    public MethodNodeEx getMethodEx(MethodIdentifier identifier) {
        return methodsEx.get(identifier);
    }

    public ClassNodeEx() {
        super(Opcodes.ASM5);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(api, descriptor);
        annotation.visible = visible;
        annotationsEx.put(descriptor, annotation);
        if (visible) {
            visibleAnnotations = Util.add(visibleAnnotations, annotation);
        } else {
            invisibleAnnotations = Util.add(invisibleAnnotations, annotation);
        }
        return annotation;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldNodeEx field = new FieldNodeEx(api, access, name, descriptor, signature, value);
        fieldsEx.put(name, field);
        fields.add(field);
        return field;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNodeEx method = new MethodNodeEx(api, access, name, descriptor, signature, exceptions);
        methodsEx.put(new MethodIdentifier(method), method);
        methods.add(method);
        return method;
    }
}
