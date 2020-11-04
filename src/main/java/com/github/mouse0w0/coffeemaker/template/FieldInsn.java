package com.github.mouse0w0.coffeemaker.template;

import org.objectweb.asm.tree.FieldInsnNode;

import java.util.Objects;

public class FieldInsn {

    private final String owner;
    private final String name;
    private final String descriptor;

    public FieldInsn(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
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

    public FieldInsnNode toInsnNode(int opcode) {
        return new FieldInsnNode(opcode, owner, name, descriptor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldInsn fieldInsn = (FieldInsn) o;
        return Objects.equals(owner, fieldInsn.owner) &&
                Objects.equals(name, fieldInsn.name) &&
                Objects.equals(descriptor, fieldInsn.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }
}
