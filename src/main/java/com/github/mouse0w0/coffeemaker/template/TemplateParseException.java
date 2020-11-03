package com.github.mouse0w0.coffeemaker.template;

public class TemplateParseException extends RuntimeException {
    public TemplateParseException(String message) {
        super(message);
    }

    public TemplateParseException(Throwable cause) {
        super("An exception has occurred when parsing template", cause);
    }

    public TemplateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
