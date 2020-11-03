package com.github.mouse0w0.coffeemaker.extree;

import org.objectweb.asm.Type;

import java.util.Objects;

public class Enum {

    private final String descriptor;
    private final String value;

    public Enum(java.lang.Enum<?> value) {
        this(Type.getDescriptor(value.getClass()), value.name());
    }

    public Enum(Type type, String value) {
        this(type.getDescriptor(), value);
    }

    public Enum(String descriptor, String value) {
        this.descriptor = descriptor;
        this.value = value;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enum anEnum = (Enum) o;
        return descriptor.equals(anEnum.descriptor) &&
                value.equals(anEnum.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptor, value);
    }
}
