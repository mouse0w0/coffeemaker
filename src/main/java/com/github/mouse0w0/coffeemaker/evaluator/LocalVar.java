package com.github.mouse0w0.coffeemaker.evaluator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalVar implements AutoCloseable {

    private final Evaluator evaluator;
    private final Map<String, Object> env;

    private final Set<String> localVariables = new HashSet<>();

    public LocalVar(Evaluator evaluator, Map<String, Object> env) {
        this.evaluator = evaluator;
        this.env = env;
    }

    public Set<String> getLocalVariables() {
        return localVariables;
    }

    public void put(String key, Object value) {
        if (env.containsKey(key) && !localVariables.contains(key)) {
            throw new AlreadyExistsVariableException(key);
        }

        env.put(key, value);
        localVariables.add(key);
    }

    @Override
    public void close() {
        evaluator.popLocalVar(this);
    }
}
