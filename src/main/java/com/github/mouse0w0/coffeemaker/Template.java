package com.github.mouse0w0.coffeemaker;

public interface Template {

    String getName();

    byte[] toByteArray(Evaluator evaluator);
}