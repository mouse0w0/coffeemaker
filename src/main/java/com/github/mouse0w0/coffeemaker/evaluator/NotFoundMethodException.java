package com.github.mouse0w0.coffeemaker.evaluator;

public class NotFoundMethodException extends EvaluatorException {
    public NotFoundMethodException(String message) {
        super(message);
    }

    public NotFoundMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
