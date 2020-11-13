package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;

public class BtValue extends BtNode {

    private final Object value;

    public BtValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object compute(Evaluator evaluator) {
        return value;
    }
}
