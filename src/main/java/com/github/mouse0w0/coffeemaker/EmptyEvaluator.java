package com.github.mouse0w0.coffeemaker;

public class EmptyEvaluator implements Evaluator {
    public static final EmptyEvaluator INSTANCE = new EmptyEvaluator();

    private EmptyEvaluator() {
    }

    @Override
    public <T> T eval(String expression) {
        return null;
    }
}
