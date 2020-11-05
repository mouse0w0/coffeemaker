package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.ModuleNode;
import org.objectweb.asm.tree.RecordComponentNode;
import org.objectweb.asm.tree.UnsupportedClassVersionException;

import java.util.*;

public class ClassNodeEx extends ClassVisitor implements AnnotationHolder {
    /**
     * The class version. The minor version is stored in the 16 most significant bits, and the major
     * version in the 16 least significant bits.
     */
    public int version;

    /**
     * The class's access flags (see {@link Opcodes}). This field also indicates if
     * the class is deprecated {@link Opcodes#ACC_DEPRECATED} or a record {@link Opcodes#ACC_RECORD}.
     */
    public int access;

    /**
     * The internal name of this class (see {@link Type#getInternalName}).
     */
    public String name;

    /**
     * The signature of this class. May be {@literal null}.
     */
    public String signature;

    /**
     * The internal of name of the super class (see {@link Type#getInternalName}).
     * For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     * {@link Object} class.
     */
    public String superName;

    /**
     * The internal names of the interfaces directly implemented by this class (see {@link
     * Type#getInternalName}).
     */
    public List<String> interfaces;

    /**
     * The name of the source file from which this class was compiled. May be {@literal null}.
     */
    public String sourceFile;

    /**
     * The correspondence between source and compiled elements of this class. May be {@literal null}.
     */
    public String sourceDebug;

    /**
     * The module stored in this class. May be {@literal null}.
     */
    public ModuleNode module;

    /**
     * The internal name of the enclosing class of this class. May be {@literal null}.
     */
    public String outerClass;

    /**
     * The name of the method that contains this class, or {@literal null} if this class is not
     * enclosed in a method.
     */
    public String outerMethod;

    /**
     * The descriptor of the method that contains this class, or {@literal null} if this class is not
     * enclosed in a method.
     */
    public String outerMethodDesc;

    public Map<String, AnnotationNodeEx> annotations;

    public Map<String, TypeAnnotationNodeEx> typeAnnotations;

    /**
     * The non standard attributes of this class. May be {@literal null}.
     */
    public List<Attribute> attrs;

    /**
     * The inner classes of this class.
     */
    public List<InnerClassNode> innerClasses;

    /**
     * The internal name of the nest host class of this class. May be {@literal null}.
     */
    public String nestHostClass;

    /**
     * The internal names of the nest members of this class. May be {@literal null}.
     */
    public List<String> nestMembers;

    /**
     * The internal names of the permitted subclasses of this class. May be {@literal null}.
     */
    public List<String> permittedSubclasses;

    /**
     * The record components of this class. May be {@literal null}.
     */
    public List<RecordComponentNode> recordComponents;

    /**
     * The fields of this class.
     */
    public Map<String, FieldNodeEx> fields;

    /**
     * The methods of this class.
     */
    public Map<Method, MethodNodeEx> methods;

    /**
     * Constructs a new {@link org.objectweb.asm.tree.ClassNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #ClassNodeEx(int)} version.
     *
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public ClassNodeEx() {
        this(Opcodes.ASM8);
    }

    /**
     * Constructs a new {@link org.objectweb.asm.tree.ClassNode}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of {@link
     *            Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6}, {@link Opcodes#ASM7} or {@link
     *            Opcodes#ASM8}.
     */
    public ClassNodeEx(final int api) {
        super(api);
        this.interfaces = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.fields = new LinkedHashMap<>();
        this.methods = new LinkedHashMap<>();
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
    public void addAnnotation(AnnotationNodeEx annotation) {
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(annotation.desc, annotation);
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

    public Collection<FieldNodeEx> getFields() {
        return fields == null ? Collections.emptyList() : fields.values();
    }

    public FieldNodeEx getField(String name) {
        return fields.get(name);
    }

    public void addField(FieldNodeEx field) {
        fields.put(field.name, field);
    }

    public Collection<MethodNodeEx> getMethods() {
        return methods == null ? Collections.emptyList() : methods.values();
    }

    public MethodNodeEx getMethod(Method method) {
        return methods.get(method);
    }

    public void addMethod(MethodNodeEx method) {
        methods.put(method.toMethod(), method);
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the ClassVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = Util.asArrayList(interfaces);
    }

    @Override
    public void visitSource(final String file, final String debug) {
        sourceFile = file;
        sourceDebug = debug;
    }

    @Override
    public ModuleVisitor visitModule(final String name, final int access, final String version) {
        module = new ModuleNode(name, access, version);
        return module;
    }

    @Override
    public void visitNestHost(final String nestHost) {
        this.nestHostClass = nestHost;
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        outerClass = owner;
        outerMethod = name;
        outerMethodDesc = descriptor;
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
    public void visitAttribute(final Attribute attribute) {
        attrs = Util.add(attrs, attribute);
    }

    @Override
    public void visitNestMember(final String nestMember) {
        nestMembers = Util.add(nestMembers, nestMember);
    }

    @Override
    public void visitPermittedSubclass(final String permittedSubclass) {
        permittedSubclasses = Util.add(permittedSubclasses, permittedSubclass);
    }

    @Override
    public void visitInnerClass(
            final String name, final String outerName, final String innerName, final int access) {
        InnerClassNode innerClass = new InnerClassNode(name, outerName, innerName, access);
        innerClasses.add(innerClass);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(
            final String name, final String descriptor, final String signature) {
        RecordComponentNode recordComponent = new RecordComponentNode(name, descriptor, signature);
        recordComponents = Util.add(recordComponents, recordComponent);
        return recordComponent;
    }

    @Override
    public FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        FieldNodeEx field = new FieldNodeEx(access, name, descriptor, signature, value);
        fields.put(name, field);
        return field;
    }

    @Override
    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        MethodNodeEx method = new MethodNodeEx(access, name, descriptor, signature, exceptions);
        methods.put(new Method(name, descriptor), method);
        return method;
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    // -----------------------------------------------------------------------------------------------
    // Accept method
    // -----------------------------------------------------------------------------------------------

    /**
     * Checks that this class node is compatible with the given ASM API version. This method checks
     * that this node, and all its children recursively, do not contain elements that were introduced
     * in more recent versions of the ASM API than the given version.
     *
     * @param api an ASM API version. Must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5},
     *            {@link Opcodes#ASM6}, {@link Opcodes#ASM7}. or {@link Opcodes#ASM8}.
     */
    public void check(final int api) {
        if (api < Opcodes.ASM9 && permittedSubclasses != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM8 && ((access & Opcodes.ACC_RECORD) != 0 || recordComponents != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM7 && (nestHostClass != null || nestMembers != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM6 && module != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM5) {
            if (typeAnnotations != null && !typeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
        // Check the annotations.
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.check(api);
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.check(api);
            }
        }
        if (recordComponents != null) {
            for (int i = recordComponents.size() - 1; i >= 0; --i) {
                recordComponents.get(i).check(api);
            }
        }
        for (int i = fields.size() - 1; i >= 0; --i) {
            fields.get(i).check(api);
        }
        for (int i = methods.size() - 1; i >= 0; --i) {
            methods.get(i).check(api);
        }
    }

    /**
     * Makes the given class visitor visit this class.
     *
     * @param classVisitor a class visitor.
     */
    public void accept(final ClassVisitor classVisitor) {
        // Visit the header.
        String[] interfacesArray = new String[this.interfaces.size()];
        this.interfaces.toArray(interfacesArray);
        classVisitor.visit(version, access, name, signature, superName, interfacesArray);
        // Visit the source.
        if (sourceFile != null || sourceDebug != null) {
            classVisitor.visitSource(sourceFile, sourceDebug);
        }
        // Visit the module.
        if (module != null) {
            module.accept(classVisitor);
        }
        // Visit the nest host class.
        if (nestHostClass != null) {
            classVisitor.visitNestHost(nestHostClass);
        }
        // Visit the outer class.
        if (outerClass != null) {
            classVisitor.visitOuterClass(outerClass, outerMethod, outerMethodDesc);
        }
        // Visit the annotations.
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.accept(classVisitor.visitAnnotation(annotation.desc, annotation.visible));
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.accept(
                        classVisitor.visitTypeAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
            }
        }
        // Visit the non standard attributes.
        if (attrs != null) {
            for (int i = 0, n = attrs.size(); i < n; ++i) {
                classVisitor.visitAttribute(attrs.get(i));
            }
        }
        // Visit the nest members.
        if (nestMembers != null) {
            for (int i = 0, n = nestMembers.size(); i < n; ++i) {
                classVisitor.visitNestMember(nestMembers.get(i));
            }
        }
        // Visit the permitted subclasses.
        if (permittedSubclasses != null) {
            for (int i = 0, n = permittedSubclasses.size(); i < n; ++i) {
                classVisitor.visitPermittedSubclass(permittedSubclasses.get(i));
            }
        }
        // Visit the inner classes.
        for (int i = 0, n = innerClasses.size(); i < n; ++i) {
            innerClasses.get(i).accept(classVisitor);
        }
        // Visit the record components.
        if (recordComponents != null) {
            for (int i = 0, n = recordComponents.size(); i < n; ++i) {
                recordComponents.get(i).accept(classVisitor);
            }
        }
        // Visit the fields.
        for (FieldNodeEx field : getFields()) {
            field.accept(classVisitor);
        }

        // Visit the methods.
        for (MethodNodeEx method : getMethods()) {
            method.accept(classVisitor);
        }
        classVisitor.visitEnd();
    }
}
