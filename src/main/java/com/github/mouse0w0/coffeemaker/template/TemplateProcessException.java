package com.github.mouse0w0.coffeemaker.template;

public class TemplateProcessException extends RuntimeException {
    public TemplateProcessException(String message) {
        super(message);
    }

    public TemplateProcessException(Throwable cause) {
        super("An exception has occurred when processing template", cause);
    }

    public TemplateProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
