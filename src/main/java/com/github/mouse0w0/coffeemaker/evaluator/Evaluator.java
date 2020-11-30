package com.github.mouse0w0.coffeemaker.evaluator;

public interface Evaluator {

    <T> T eval(String expression) throws EvaluatorException;

    void addVariable(String key, Object value) throws AlreadyExistsVariableException;

    void removeVariable(String key);
}
