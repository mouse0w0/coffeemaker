package com.github.mouse0w0.coffeemaker.util;


import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;

import java.lang.reflect.Method;

public class ASMUtils {

    public static String getMethodId(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return getMethodId(clazz.getMethod(name, parameterTypes));
        } catch (NoSuchMethodException e) {
            throw new NullPointerException("No such method " + name);
        }
    }

    public static String getMethodId(Method method) {
        return Type.getInternalName(method.getDeclaringClass()) + "." + method.getName() + Type.getMethodDescriptor(method);
    }

    public static String getMethodId(MethodInsnNode insnNode) {
        return insnNode.owner + "." + insnNode.name + insnNode.desc;
    }
}
