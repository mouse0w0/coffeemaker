package com.github.mouse0w0.coffeemaker.template.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;

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

    @Override
    public Object computeNonNull(Evaluator evaluator) {
        if (value == null) {
            throw new TemplateProcessException("The value cannot be null");
        }
        return value;
    }

    @Override
    public String toString() {
        return "BtValue{" +
                "value=" + value +
                '}';
    }
}
