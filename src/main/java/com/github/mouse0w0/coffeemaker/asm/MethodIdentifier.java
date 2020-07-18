package com.github.mouse0w0.coffeemaker.asm;

import com.github.mouse0w0.coffeemaker.util.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.Objects;

public class MethodIdentifier {

    private final String owner;
    private final String name;
    private final String descriptor;

    public MethodIdentifier(Method method) {
        this(Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
    }

    public MethodIdentifier(MethodNode methodNode) {
        this(null, methodNode.name, methodNode.desc);
    }

    public MethodIdentifier(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = Objects.requireNonNull(name, "name");
        this.descriptor = Objects.requireNonNull(descriptor, "description");
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public MethodNode get(ClassNode classNode) {
        return ASMUtils.getMethod(classNode, name, descriptor);
    }

    public boolean equals(MethodNode methodNode) {
        return equals(methodNode.name, methodNode.desc);
    }

    public boolean equals(MethodInsnNode methodInsnNode) {
        return equals(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }

    public boolean equals(String name, String descriptor) {
        return this.name.equals(name) && this.descriptor.equals(descriptor);
    }

    public boolean equals(String owner, String name, String descriptor) {
        return equals(name, descriptor) && this.owner.equals(owner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodIdentifier that = (MethodIdentifier) o;
        return name.equals(that.name) &&
                descriptor.equals(that.descriptor)
                && (owner == null || that.owner == null || owner.equals(that.owner));
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }
}
