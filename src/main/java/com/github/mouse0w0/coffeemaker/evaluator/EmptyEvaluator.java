package com.github.mouse0w0.coffeemaker.evaluator;

public class EmptyEvaluator implements Evaluator {
    public static final EmptyEvaluator INSTANCE = new EmptyEvaluator();

    private EmptyEvaluator() {
    }

    @Override
    public <T> T eval(String expression) {
        return null;
    }

    @Override
    public void addVariable(String key, Object value) {
        throw new UnsupportedOperationException("empty");
    }

    @Override
    public void removeVariable(String key) {
        throw new UnsupportedOperationException("empty");
    }
}
