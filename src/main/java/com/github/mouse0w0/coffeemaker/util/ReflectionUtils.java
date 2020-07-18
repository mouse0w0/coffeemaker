package com.github.mouse0w0.coffeemaker.util;

import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
