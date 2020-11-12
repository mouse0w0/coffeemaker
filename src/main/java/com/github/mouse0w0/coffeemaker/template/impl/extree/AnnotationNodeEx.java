package com.github.mouse0w0.coffeemaker.template.impl.extree;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnnotationNodeEx extends AnnotationVisitor {

    public String desc;
    public boolean visible;

    public Map<String, Object> values;

    List<Object> array;

    public AnnotationNodeEx(final String descriptor, final boolean visible) {
        this(/* latest api = */ Opcodes.ASM9, descriptor, visible);
    }

    public AnnotationNodeEx(final int api, final String descriptor, final boolean visible) {
        super(api);
        this.desc = descriptor;
        this.visible = visible;
    }

    AnnotationNodeEx(final List<Object> array) {
        super(/* latest api = */ Opcodes.ASM9);
        this.array = array;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) {
        return values == null ? null : (T) values.get(name);
    }

    public <T> T getValue(String name, T defaultValue) {
        return values == null ? defaultValue : (T) values.getOrDefault(name, defaultValue);
    }

    public void putValue(String name, Object value) {
        if (desc != null) {
            if (values == null) {
                values = new LinkedHashMap<>(2);
            }
            if (value instanceof byte[]) {
                values.put(name, Util.asArrayList((byte[]) value));
            } else if (value instanceof boolean[]) {
                values.put(name, Util.asArrayList((boolean[]) value));
            } else if (value instanceof short[]) {
                values.put(name, Util.asArrayList((short[]) value));
            } else if (value instanceof char[]) {
                values.put(name, Util.asArrayList((char[]) value));
            } else if (value instanceof int[]) {
                values.put(name, Util.asArrayList((int[]) value));
            } else if (value instanceof long[]) {
                values.put(name, Util.asArrayList((long[]) value));
            } else if (value instanceof float[]) {
                values.put(name, Util.asArrayList((float[]) value));
            } else if (value instanceof double[]) {
                values.put(name, Util.asArrayList((double[]) value));
            } else {
                values.put(name, value);
            }
        } else {
            if (value instanceof byte[]) {
                array.add(Util.asArrayList((byte[]) value));
            } else if (value instanceof boolean[]) {
                array.add(Util.asArrayList((boolean[]) value));
            } else if (value instanceof short[]) {
                array.add(Util.asArrayList((short[]) value));
            } else if (value instanceof char[]) {
                array.add(Util.asArrayList((char[]) value));
            } else if (value instanceof int[]) {
                array.add(Util.asArrayList((int[]) value));
            } else if (value instanceof long[]) {
                array.add(Util.asArrayList((long[]) value));
            } else if (value instanceof float[]) {
                array.add(Util.asArrayList((float[]) value));
            } else if (value instanceof double[]) {
                array.add(Util.asArrayList((double[]) value));
            } else {
                array.add(value);
            }
        }
    }

    // ------------------------------------------------------------------------
    // Implementation of the AnnotationVisitor abstract class
    // ------------------------------------------------------------------------

    @Override
    public void visit(final String name, final Object value) {
        putValue(name, value);
    }

    @Override
    public void visitEnum(final String name, final String descriptor, final String value) {
        putValue(name, new Enum(descriptor, value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String descriptor) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor, false);
        putValue(name, annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        List<Object> array = new ArrayList<>();
        putValue(name, array);
        return new AnnotationNodeEx(array);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    // ------------------------------------------------------------------------
    // Accept methods
    // ------------------------------------------------------------------------

    /**
     * Checks that this annotation node is compatible with the given ASM API version. This method
     * checks that this node, and all its children recursively, do not contain elements that were
     * introduced in more recent versions of the ASM API than the given version.
     *
     * @param api an ASM API version. Must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5},
     *            {@link Opcodes#ASM6}, {@link Opcodes#ASM7} or {@link Opcodes#ASM9}.
     */
    public void check(final int api) {
        // nothing to do
    }

    /**
     * Makes the given visitor visit this annotation.
     *
     * @param annotationVisitor an annotation visitor. Maybe {@literal null}.
     */
    public void accept(final AnnotationVisitor annotationVisitor) {
        if (annotationVisitor == null) return;

        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                accept(annotationVisitor, entry.getKey(), entry.getValue());
            }
        }
        annotationVisitor.visitEnd();
    }

    static void accept(
            final AnnotationVisitor annotationVisitor, final String name, final Object value) {
        if (annotationVisitor == null) return;

        if (value instanceof Enum) {
            Enum anEnum = (Enum) value;
            annotationVisitor.visitEnum(name, anEnum.getDescriptor(), anEnum.getValue());
        } else if (value instanceof AnnotationNodeEx) {
            AnnotationNodeEx annotationValue = (AnnotationNodeEx) value;
            annotationValue.accept(annotationVisitor.visitAnnotation(name, annotationValue.desc));
        } else if (value instanceof List) {
            AnnotationVisitor arrayAnnotationVisitor = annotationVisitor.visitArray(name);
            if (arrayAnnotationVisitor != null) {
                List<?> arrayValue = (List<?>) value;
                for (int i = 0, n = arrayValue.size(); i < n; ++i) {
                    accept(arrayAnnotationVisitor, null, arrayValue.get(i));
                }
                arrayAnnotationVisitor.visitEnd();
            }
        } else {
            annotationVisitor.visit(name, value);
        }
    }
}
