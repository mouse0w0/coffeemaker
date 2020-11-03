package com.github.mouse0w0.coffeemaker;

public abstract class BaseEvaluator implements Evaluator {
    private final Evaluator parent;

    public BaseEvaluator() {
        this(null);
    }

    public BaseEvaluator(Evaluator parent) {
        this.parent = parent;
    }

    @Override
    public <T> T eval(String statement) {
        return parent != null ? parent.eval(statement) : null;
    }
}
