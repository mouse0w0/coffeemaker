package com.github.mouse0w0.coffeemaker.asm;

import org.objectweb.asm.Type;

import java.util.Objects;

public class EnumInfo {

    private final String descriptor;
    private final String value;

    public EnumInfo(Enum<?> value) {
        this(Type.getDescriptor(value.getClass()), value.name());
    }

    public EnumInfo(String descriptor, String value) {
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
        EnumInfo enumInfo = (EnumInfo) o;
        return descriptor.equals(enumInfo.descriptor) &&
                value.equals(enumInfo.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptor, value);
    }
}
