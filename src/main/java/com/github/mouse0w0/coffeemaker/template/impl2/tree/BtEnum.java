package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import org.objectweb.asm.Type;

public class BtEnum extends BtObject {
    public static final String DESCRIPTOR = "descriptor";
    public static final String VALUE = "value";

    public BtEnum() {
    }

    public BtEnum(java.lang.Enum<?> value) {
        this(Type.getDescriptor(value.getClass()), value.name());
    }

    public BtEnum(Type type, String value) {
        this(type.getDescriptor(), value);
    }

    public BtEnum(String descriptor, String value) {
        putValue(DESCRIPTOR, descriptor);
        putValue(VALUE, value);
    }
}
