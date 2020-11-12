package com.github.mouse0w0.coffeemaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleEvaluator extends BaseEvaluator {
    private static final Pattern SYNTAX = Pattern.compile("\\.");

    private final Object dataModel;

    public SimpleEvaluator(Object dataModel) {
        this(null, dataModel);
    }

    public SimpleEvaluator(Evaluator parent, Object dataModel) {
        super(parent);
        this.dataModel = dataModel;
    }

    @Override

    public <T> T eval(String expression) {
        T obj = eval0(expression);
        return obj != null ? obj : super.eval(expression);
    }

    @SuppressWarnings("unchecked")
    protected <T> T eval0(String statement) {
        Object obj = dataModel;
        for (String s : SYNTAX.split(statement)) {
            obj = eval1(obj, s);
        }
        return (T) obj;
    }

    private Object eval1(Object obj, String name) {
        if (obj instanceof Map) {
            return ((Map) obj).get(name);
        }

        String getterName = "get" + Character.toUpperCase(name.indexOf(0)) + name.substring(1);
        Class<?> clazz = obj.getClass();
        try {
            Method method = clazz.getMethod(getterName);
            return method.invoke(obj);
        } catch (ReflectiveOperationException ignored) {
        }

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
