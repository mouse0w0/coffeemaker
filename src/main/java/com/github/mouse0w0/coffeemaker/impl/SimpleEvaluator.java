package com.github.mouse0w0.coffeemaker.impl;

import com.github.mouse0w0.coffeemaker.Evaluator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleEvaluator implements Evaluator {
    private static final Pattern SYNTAX = Pattern.compile("\\.");

    private Object dataModel;

    public SimpleEvaluator(Object dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void setContext(Object dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public <T> T eval(String statement) {
        Object obj = dataModel;
        for (String s : SYNTAX.split(statement)) {
            obj = get(obj, s);
        }
        return (T) obj;
    }

    private Object get(Object obj, String name) {
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
