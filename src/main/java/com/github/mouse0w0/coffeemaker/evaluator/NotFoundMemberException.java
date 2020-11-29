package com.github.mouse0w0.coffeemaker.evaluator;

public class NotFoundMemberException extends EvaluatorException {
    public NotFoundMemberException(String message) {
        super(message);
    }

    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
