package com.github.mouse0w0.coffeemaker.exception;

public class TemplateLoadException extends RuntimeException {
    public TemplateLoadException(String message) {
        super(message);
    }

    public TemplateLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
