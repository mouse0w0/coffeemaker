package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import java.lang.reflect.Method;

public class Utils {
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameters) {
        try {
            return clazz.getDeclaredMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
