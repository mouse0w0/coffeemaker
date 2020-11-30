package com.github.mouse0w0.coffeemaker.evaluator;

public class AlreadyExistsVariableException extends EvaluatorException {
    public AlreadyExistsVariableException(String variableName) {
        super("The variable \"" + variableName + "\" already exists");
    }
}
