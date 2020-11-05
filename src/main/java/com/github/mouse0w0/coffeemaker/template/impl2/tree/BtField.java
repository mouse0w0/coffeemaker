package com.github.mouse0w0.coffeemaker.template.impl2.tree;

public class BtField extends BtObject {
    public static final String ACCESS = "access";
    public static final String NAME = "name";
    public static final String DESCRIPTOR = "descriptor";
    public static final String SIGNATURE = "signature";
    public static final String VALUE = "value";

    public static final String ANNOTATIONS = "annotations";

    public static final String TYPE_ANNOTATIONS = "typeAnnotations";

    public static final String ATTRIBUTES = "attributes";

    public BtField(int access, String name, String descriptor, String signature, Object value) {
        putValue(ACCESS, access);
        putValue(NAME, name);
        putValue(DESCRIPTOR, descriptor);
        putValue(SIGNATURE, signature);
        putValue(VALUE, value);
    }

    public BtField() {
    }
}
