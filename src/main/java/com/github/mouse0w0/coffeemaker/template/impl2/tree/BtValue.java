package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import java.util.Collection;

public class BtValue extends BtNode {

    private final Object value;

    public BtValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public int getAsInt() {
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new IllegalStateException("The type of value is not String");
    }

    public boolean getAsBoolean() {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new IllegalStateException("The type of value is not String");
    }

    public String getAsString() {
        if (value instanceof String) {
            return (String) value;
        }
        throw new IllegalStateException("The type of value is not String");
    }

    @SuppressWarnings("unchecked")
    public String[] getAsStringArray() {
        if (value instanceof Collection) {
            return ((Collection<String>) value).toArray(new String[0]);
        } else if (value instanceof String[]) {
            return (String[]) value;
        }
        throw new IllegalStateException("The type of value is not String[]");
    }
}
