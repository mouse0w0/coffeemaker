package com.github.mouse0w0.coffeemaker.template.impl2.tree;

public class BtInnerClass extends BtObject {

    public static final String NAME = "name";
    public static final String OUTER_NAME = "outerName";
    public static final String INNER_NAME = "innerName";
    public static final String ACCESS = "access";

    public BtInnerClass(String name, String outerName, String innerName, int access) {
        putValue(NAME, name);
        putValue(OUTER_NAME, outerName);
        putValue(INNER_NAME, innerName);
        putValue(ACCESS, access);
    }

    public BtInnerClass() {
    }
}
