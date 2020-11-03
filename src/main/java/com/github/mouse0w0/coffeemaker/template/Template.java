package com.github.mouse0w0.coffeemaker.template;

import com.github.mouse0w0.coffeemaker.Evaluator;

public interface Template {

    String getName();

    byte[] process(Evaluator evaluator);
}