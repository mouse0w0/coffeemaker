package com.github.mouse0w0.coffeemaker.evaluator;

public class EvaluatorException extends RuntimeException {
    public EvaluatorException(String message) {
        super(message);
    }

    public EvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
