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
    public Object eval(String expression) {
        return null;
    }

    @Override
    public <T> T eval(String expression, Class<T> returnType) throws EvaluatorException {
        Object eval = eval(expression);
        try {
            return returnType.cast(eval);
        } catch (ClassCastException e) {
            throw new EvaluatorException("An exception occurred when evaluating script: \n" + expression, e);
        }
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
