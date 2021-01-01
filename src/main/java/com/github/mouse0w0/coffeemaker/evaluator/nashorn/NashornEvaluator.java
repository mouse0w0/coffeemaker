package com.github.mouse0w0.coffeemaker.evaluator.nashorn;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.EvaluatorException;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.regex.Pattern;

public final class NashornEvaluator implements Evaluator {
    private static final Pattern USE_FUNCTION = Pattern.compile("^[\"']use function[\"'];");

    private final ScriptEngine engine;
    private final Bindings bindings;

    private final ArrayDeque<LocalVar> localVars = new ArrayDeque<>();

    private final Invocable invocable;

    public NashornEvaluator() {
        engine = new NashornScriptEngineFactory().getScriptEngine();
        invocable = (Invocable) engine;
        bindings = engine.createBindings();
        engine.getContext().setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
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
    public Object eval(String expression) throws EvaluatorException {
        try {
            if (USE_FUNCTION.matcher(expression).find()) {
                engine.eval(expression);
                return invocable.invokeFunction("main");
            } else {
                return engine.eval(expression);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            throw new EvaluatorException("An exception occurred when evaluating script: \n" + expression, e);
        }
    }

    @Override
    public <T> T eval(String expression, Class<T> returnType) throws EvaluatorException {
        Object eval = eval(expression);
        try {
            return returnType.cast(eval);
        } catch (ClassCastException e) {
            throw new EvaluatorException("An exception occurred when evaluating script: \n" + expression, e);
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
