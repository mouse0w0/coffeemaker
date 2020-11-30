package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.tree.BtNode;

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
    public Object computeNonNull(Evaluator evaluator) {
        Object value = evaluator.eval(expression);
        if (value == null) {
            throw new TemplateProcessException("The value of expression \"" + expression + "\" cannot be null");
        }
        return value;
    }

    @Override
    public String toString() {
        return "BtComputableValue{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
