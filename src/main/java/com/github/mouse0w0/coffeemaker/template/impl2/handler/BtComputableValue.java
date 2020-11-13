package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtNode;

public class BtComputableValue extends BtNode {
    private final String expression;

    public BtComputableValue(String expression) {
        this.expression = expression;
    }

    @Override
    public Object compute(Evaluator evaluator) {
        return evaluator.eval(expression);
    }

    @Override
    public int computeInt(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new TemplateProcessException("The type of value is not Integer");
    }

    @Override
    public boolean computeBoolean(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new TemplateProcessException("The type of value is not Boolean");
    }

    @Override
    public String computeString(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        throw new TemplateProcessException("The type of value is not String");
    }
}
