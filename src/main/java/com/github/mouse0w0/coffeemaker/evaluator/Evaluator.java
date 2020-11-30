package com.github.mouse0w0.coffeemaker.evaluator;

import java.util.Map;

public interface Evaluator {

    Map<String, Object> getEnv();

    <T> T eval(String expression) throws EvaluatorException;

    void addVariable(String key, Object value) throws AlreadyExistsVariableException;

    void removeVariable(String key);
}
