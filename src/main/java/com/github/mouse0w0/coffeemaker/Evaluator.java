package com.github.mouse0w0.coffeemaker;

public interface Evaluator {

    <T> T eval(String expression);
}
