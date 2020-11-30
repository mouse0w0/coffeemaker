package com.github.mouse0w0.coffeemaker.evaluator;

import java.util.Collections;
import java.util.Map;

public class EmptyEvaluator implements Evaluator {
    public static final EmptyEvaluator INSTANCE = new EmptyEvaluator();

    private EmptyEvaluator() {
    }

    @Override
    public Map<String, Object> getEnv() {
        return Collections.emptyMap();
    }

    @Override
    public <T> T eval(String expression) {
        return null;
    }

    @Override
    public LocalVar pushLocalVar() {
        return new LocalVar(this, Collections.emptyMap());
    }

    @Override
    public void popLocalVar() {

    }

    @Override
    public void popLocalVar(LocalVar localVar) {

    }
}
