package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.UnsupportedClassVersionException;

import java.util.*;

public class FieldNodeEx extends FieldVisitor implements AnnotationHolder {

    /**
     * The field's access flags (see {@link Opcodes}). This field also indicates if
     * the field is synthetic and/or deprecated.
     */
    public int access;

    /**
     * The field's name.
     */
    public String name;

    /**
     * The field's descriptor (see {@link Type}).
     */
    public String desc;

    /**
     * The field's signature. May be {@literal null}.
     */
    public String signature;

    /**
     * The field's initial value. This field, which may be {@literal null} if the field does not have
     * an initial value, must be an {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double}
     * or a {@link String}.
     */
    public Object value;

    public Map<String, AnnotationNodeEx> annotations;

    public Map<String, TypeAnnotationNodeEx> typeAnnotations;

    /**
     * The non standard attributes of this field. * May be {@literal null}.
     */
    public List<Attribute> attrs;

    private ClassNodeEx owner;

    /**
     * Constructs a new {@link FieldNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #FieldNodeEx(int, int, String, String, String, Object)} version.
     *
     * @param access     the field's access flags (see {@link Opcodes}). This parameter
     *                   also indicates if the field is synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature  the field's signature.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the
     *                   field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *                   Long}, a {@link Double} or a {@link String}.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public FieldNodeEx(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        this(/* latest api = */ Opcodes.ASM9, access, name, descriptor, signature, value);
    }

    /**
     * Constructs a new {@link FieldNode}.
     *
     * @param api        the ASM API version implemented by this visitor.
     * @param access     the field's access flags (see {@link Opcodes}). This parameter
     *                   also indicates if the field is synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature  the field's signature.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the
     *                   field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *                   Long}, a {@link Double} or a {@link String}.
     */
    public FieldNodeEx(
            final int api,
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        super(api);
        this.access = access;
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
        this.value = value;
    }

    public ClassNodeEx getOwner() {
        return owner;
    }

    void setOwner(ClassNodeEx owner) {
        this.owner = owner;
    }

    @Override
    public Collection<AnnotationNodeEx> getAnnotations() {
        return annotations == null ? Collections.emptyList() : annotations.values();
    }

    @Override
    public AnnotationNodeEx getAnnotation(String descriptor) {
        return annotations == null ? null : annotations.get(descriptor);
    }

    @Override
    public void addAnnotation(AnnotationNodeEx annotationNode) {
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(annotationNode.desc, annotationNode);
    }

    @Override
    public void removeAnnotation(String descriptor) {
        if (annotations == null) return;
        annotations.remove(descriptor);
    }

    @Override
    public void removeAnnotation(AnnotationNodeEx annotation) {
        removeAnnotation(annotation.desc);
    }

    public Collection<TypeAnnotationNodeEx> getTypeAnnotations() {
        return typeAnnotations == null ? Collections.emptyList() : typeAnnotations.values();
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

    // -----------------------------------------------------------------------------------------------
    // Implementation of the FieldVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor, visible);
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(descriptor, annotation);
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
    public void visitAttribute(final Attribute attribute) {
        attrs = Util.add(attrs, attribute);
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    // -----------------------------------------------------------------------------------------------
    // Accept methods
    // -----------------------------------------------------------------------------------------------

    /**
     * Checks that this field node is compatible with the given ASM API version. This method checks
     * that this node, and all its children recursively, do not contain elements that were introduced
     * in more recent versions of the ASM API than the given version.
     *
     * @param api an ASM API version. Must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5},
     *            {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     */
    public void check(final int api) {
        if (api == Opcodes.ASM4) {
            if (typeAnnotations != null && !typeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
    }

    /**
     * Makes the given class visitor visit this field.
     *
     * @param classVisitor a class visitor.
     */
    public void accept(final ClassVisitor classVisitor) {
        FieldVisitor fieldVisitor = classVisitor.visitField(access, name, desc, signature, value);
        if (fieldVisitor == null) {
            return;
        }
        // Visit the annotations.
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.accept(fieldVisitor.visitAnnotation(annotation.desc, annotation.visible));
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.accept(
                        fieldVisitor.visitTypeAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
            }
        }
        // Visit the non standard attributes.
        if (attrs != null) {
            for (int i = 0, n = attrs.size(); i < n; ++i) {
                fieldVisitor.visitAttribute(attrs.get(i));
            }
        }
        fieldVisitor.visitEnd();
    }
}
