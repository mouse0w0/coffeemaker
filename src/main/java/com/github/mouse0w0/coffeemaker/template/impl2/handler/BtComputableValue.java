package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
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
}
