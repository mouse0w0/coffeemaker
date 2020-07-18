package com.github.mouse0w0.coffeemaker;

public interface Evaluator {

    void setContext(Object dataModel);

    <T> T eval(String statement);
}
