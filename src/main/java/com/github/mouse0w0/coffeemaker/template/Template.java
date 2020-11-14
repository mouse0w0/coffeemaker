package com.github.mouse0w0.coffeemaker.template;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;

public interface Template {

    String getName();

    byte[] process(Evaluator evaluator);

    byte[] process(String newClassName, Evaluator evaluator);
}