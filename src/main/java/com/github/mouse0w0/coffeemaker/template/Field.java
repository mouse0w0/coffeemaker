package com.github.mouse0w0.coffeemaker.template;

import com.github.mouse0w0.coffeemaker.template.tree.insn.BtFieldInsn;
import org.objectweb.asm.Type;

import java.util.Objects;

public class Field {

    private final String owner;
    private final String name;
    private final String descriptor;

    public Field(Enum<?> value) {
        this(Type.getType(value.getClass()), value.name());
    }

    public Field(Type type, String name) {
        this(type.getInternalName(), name, type.getDescriptor());
    }

    public Field(BtFieldInsn fieldInsn) {
        this(fieldInsn.get(BtFieldInsn.OWNER).getAsString(),
                fieldInsn.get(BtFieldInsn.NAME).getAsString(),
                fieldInsn.get(BtFieldInsn.DESCRIPTOR).getAsString());
    }

    public Field(String owner, String name, String descriptor) {
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

    public BtFieldInsn toInsnNode(int opcode) {
        return new BtFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(owner, field.owner) &&
                Objects.equals(name, field.name) &&
                Objects.equals(descriptor, field.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }

    @Override
    public String toString() {
        return "Field{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}
