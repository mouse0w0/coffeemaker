package com.github.mouse0w0.coffeemaker.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationNodeEx extends AnnotationNode {
    private final Map<String, Object> valuesEx = new HashMap<>();

    public boolean visible;

    public AnnotationNodeEx(String s) {
        this(Opcodes.ASM5, s);
    }

    public AnnotationNodeEx(int i, String s) {
        super(i, s);
    }

    public Map<String, Object> getValuesEx() {
        return valuesEx;
    }

    public <T> T getValueEx(String name) {
        return (T) valuesEx.get(name);
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        valuesEx.put(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(name, descriptor, value);
        valuesEx.put(name, new EnumInfo(descriptor, value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        if (values == null) {
            values = new ArrayList<>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor);
        values.add(annotation);
        valuesEx.put(name, annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        if (values == null) {
            values = new ArrayList<>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        List<Object> array = new ArrayList<>();
        values.add(array);
        valuesEx.put(name, array);
        AnnotationNodeEx annotation = new AnnotationNodeEx(null);
        annotation.values = array;
        return annotation;
    }
}
