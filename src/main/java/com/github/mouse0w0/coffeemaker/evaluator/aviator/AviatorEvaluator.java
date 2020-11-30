package com.github.mouse0w0.coffeemaker.evaluator.aviator;

import com.github.mouse0w0.coffeemaker.evaluator.AlreadyExistsVariableException;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.EvaluatorException;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.HashMap;
import java.util.Map;

public class AviatorEvaluator implements Evaluator {
    private final AviatorEvaluatorInstance instance;
    private final Map<String, Object> env;

    public AviatorEvaluator() {
        this(AviatorHelper.getInstance(), new HashMap<>());
    }

    public AviatorEvaluator(Map<String, Object> env) {
        this(AviatorHelper.getInstance(), env);
    }

    public AviatorEvaluator(AviatorEvaluatorInstance instance, Map<String, Object> env) {
        this.instance = instance;
        this.env = env;
    }

    @Override
    public Map<String, Object> getEnv() {
        return env;
    }

    @Override
    public <T> T eval(String expression) throws EvaluatorException {
        return (T) instance.execute(expression, env);
    }

    @Override
    public void addVariable(String key, Object value) throws AlreadyExistsVariableException {
        if (env.putIfAbsent(key, value) != null) {
            throw new AlreadyExistsVariableException(key);
        }
    }

    @Override
    public void removeVariable(String key) {
        env.remove(key);
    }
}
