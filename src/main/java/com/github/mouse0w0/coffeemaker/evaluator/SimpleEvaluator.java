package com.github.mouse0w0.coffeemaker.evaluator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleEvaluator extends BaseEvaluator {
    private static final Pattern SEPARATOR = Pattern.compile("\\.");

    private static final Pattern METHOD = Pattern.compile("^([a-zA-Z$_][0-9a-zA-Z$_]*)\\(\\)$");

    protected final Object dataModel;

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
    protected <T> T eval0(String expression) {
        Object obj = dataModel;
        for (String s : SEPARATOR.split(expression)) {
            obj = eval1(obj, s);
        }
        return (T) obj;
    }

    @SuppressWarnings("rawtypes")
    private Object eval1(Object obj, String name) {
        if (METHOD.matcher(name).matches()) {
            return evalMethod(obj, name);
        } else {
            return evalValue(obj, name);
        }
    }

    private Object evalMethod(Object obj, String name) {
        String methodName = name.substring(0, name.indexOf('('));
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            return method.invoke(obj);
        } catch (NoSuchMethodException e) {
            throw new NotFoundMethodException("Not found method of \"" + name + "\"", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EvaluatorException("Caught an exception when eval \"" + name + "\"", e);
        }
    }

    private Object evalValue(Object obj, String name) {
        if (name == null || name.isEmpty()) {
            return obj;
        }

        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(name)) {
                return map.get(name);
            } else {
                throw new NotFoundMemberException("Not found value of \"" + name + "\"");
            }
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
        } catch (NoSuchFieldException e) {
            throw new NotFoundMemberException("No such field of \"" + name + "\"", e);
        } catch (IllegalAccessException e) {
            throw new EvaluatorException("Caught an exception when eval \"" + name + "\"", e);
        }
    }
}
