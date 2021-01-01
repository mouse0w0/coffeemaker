package com.github.mouse0w0.coffeemaker.evaluator;

import java.util.Map;

public interface Evaluator {

    Map<String, Object> getEnv();

    Object eval(String expression) throws EvaluatorException;

    <T> T eval(String expression, Class<T> returnType) throws EvaluatorException;

    LocalVar pushLocalVar();

    void popLocalVar();

    void popLocalVar(LocalVar localVar);
}
