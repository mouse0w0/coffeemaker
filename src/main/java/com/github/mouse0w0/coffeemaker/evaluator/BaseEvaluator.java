package com.github.mouse0w0.coffeemaker.evaluator;

public abstract class BaseEvaluator implements Evaluator {
    protected final Evaluator parent;

    public BaseEvaluator() {
        this(null);
    }

    public BaseEvaluator(Evaluator parent) {
        this.parent = parent;
    }

    @Override
    public <T> T eval(String expression) {
        return parent != null ? parent.eval(expression) : null;
    }
}
