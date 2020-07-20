package com.github.mouse0w0.coffeemaker.exception;

public class TemplateParseException extends RuntimeException {
    public TemplateParseException(String message) {
        super(message);
    }

    public TemplateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
