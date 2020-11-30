package com.github.mouse0w0.coffeemaker.evaluator;

import java.util.Map;

public interface Evaluator {

    Map<String, Object> getEnv();

    <T> T eval(String expression) throws EvaluatorException;

    LocalVar pushLocalVar();

    void popLocalVar();

    void popLocalVar(LocalVar localVar);
}
