package com.github.mouse0w0.coffeemaker.template.impl2.tree;

public class BtAnnotation extends BtObject {
    public static final String DESCRIPTOR = "descriptor";
    public static final String VISIBLE = "visible";
    public static final String VALUES = "values";

    public BtAnnotation(String descriptor, boolean visible) {
        putValue(DESCRIPTOR, descriptor);
        putValue(VISIBLE, visible);
    }

    public BtAnnotation() {
    }
}
