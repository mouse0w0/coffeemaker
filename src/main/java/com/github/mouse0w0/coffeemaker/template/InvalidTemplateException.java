package com.github.mouse0w0.coffeemaker.template;

public class InvalidTemplateException extends TemplateParseException {
    public InvalidTemplateException(String className) {
        super("The class " + className + " is not a template");
    }
}
