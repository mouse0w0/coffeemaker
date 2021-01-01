package com.github.mouse0w0.coffeemaker.evaluator.nashorn;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.EvaluatorException;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayDeque;
import java.util.Map;

public final class NashornEvaluator implements Evaluator {
    private final ScriptEngine engine;
    private final Bindings bindings;

    private final ArrayDeque<LocalVar> localVars = new ArrayDeque<>();

    public NashornEvaluator() {
        engine = new NashornScriptEngineFactory().getScriptEngine();
        bindings = engine.createBindings();
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public Map<String, Object> getEnv() {
        return bindings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T eval(String expression) throws EvaluatorException {
        try {
            return (T) engine.eval(expression, bindings);
        } catch (ScriptException e) {
            throw new EvaluatorException("An exception occurred when evaluating script", e);
        }
    }

    @Override
    public LocalVar pushLocalVar() {
        LocalVar localVar = new LocalVar(this, bindings);
        localVars.addLast(localVar);
        return localVar;
    }

    @Override
    public void popLocalVar() {
        popLocalVar(localVars.removeLast());
    }

    @Override
    public void popLocalVar(LocalVar localVar) {
        localVar.getLocalVariables().forEach(bindings::remove);
    }
}
