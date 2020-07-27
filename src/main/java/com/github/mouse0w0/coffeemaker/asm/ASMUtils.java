package com.github.mouse0w0.coffeemaker.asm;


import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASMUtils {

    public static AnnotationNode getVisibleAnnotation(ClassNode clazz, Type type) {
        return getAnnotation(clazz, type, false);
    }

    public static AnnotationNode getVisibleAnnotation(ClassNode clazz, String type) {
        return getAnnotation(clazz, type, false);
    }

    public static AnnotationNode getInvisibleAnnotation(ClassNode clazz, Type type) {
        return getAnnotation(clazz, type, true);
    }

    public static AnnotationNode getInvisibleAnnotation(ClassNode clazz, String type) {
        return getAnnotation(clazz, type, true);
    }

    public static AnnotationNode getAnnotation(ClassNode clazz, Type type, boolean invisible) {
        return getAnnotation(clazz, type.getDescriptor(), invisible);
    }

    public static AnnotationNode getAnnotation(ClassNode clazz, String type, boolean invisible) {
        List<AnnotationNode> annotations = invisible ? clazz.invisibleAnnotations : clazz.visibleAnnotations;
        if (annotations == null) {
            return null;
        }

        for (AnnotationNode annotation : annotations) {
            if (annotation.desc.equals(type)) {
                return annotation;
            }
        }
        return null;
    }

    public static MethodNode getMethod(ClassNode clazz, String name, String desc) {
        for (MethodNode method : clazz.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return method;
            }
        }
        return null;
    }

    public static Map<String, Object> getAnnotationValues(AnnotationNode annotation) {
        Map<String, Object> valueMap = new HashMap<>();
        List<Object> values = annotation.values;
        for (int i = 0; i < values.size(); i += 2) {
            valueMap.put((String) values.get(i), values.get(i + 1));
        }
        return valueMap;
    }

    public static <T> T getAnnotationValue(AnnotationNode annotation, String name) {
        List<Object> values = annotation.values;
        for (int i = 0; i < values.size(); i += 2) {
            if (values.get(i).equals(name)) {
                return (T) values.get(i + 1);
            }
        }
        return null;
    }

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

    public static String getMethodId(MethodNode method) {
        return method.name + method.desc;
    }

    public static String getMethodId(MethodInsnNode insnNode) {
        return insnNode.owner + "." + insnNode.name + insnNode.desc;
    }
}
