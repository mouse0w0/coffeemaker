package com.github.mouse0w0.coffeemaker.template.impl;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;

public class Utils {

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

    public static AbstractInsnNode createInsnNode(boolean value) {
        return new InsnNode(value ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
    }

    public static AbstractInsnNode createInsnNode(int value) {
        if (value >= -1 && value <= 5) {
            return new InsnNode(Opcodes.ICONST_0 + value);
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return new IntInsnNode(Opcodes.BIPUSH, value);
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return new IntInsnNode(Opcodes.SIPUSH, value);
        } else {
            return new LdcInsnNode(value);
        }
    }

    public static AbstractInsnNode createInsnNode(long value) {
        if (value == 0L || value == 1L) {
            return new InsnNode(Opcodes.LCONST_0 + (int) value);
        } else {
            return new LdcInsnNode(value);
        }
    }

    public static AbstractInsnNode createInsnNode(float value) {
        int bits = Float.floatToIntBits(value);
        if (bits == 0L || bits == 0x3F800000 || bits == 0x40000000) { // 0..2
            return new InsnNode(Opcodes.FCONST_0 + (int) value);
        } else {
            return new LdcInsnNode(value);
        }
    }

    public static AbstractInsnNode createInsnNode(double value) {
        long bits = Double.doubleToLongBits(value);
        if (bits == 0L || bits == 0x3FF0000000000000L) { // +0.0d and 1.0d
            return new InsnNode(Opcodes.DCONST_0 + (int) value);
        } else {
            return new LdcInsnNode(value);
        }
    }

    public static AbstractInsnNode createInsnNode(final String value) {
        if (value == null) {
            return new InsnNode(Opcodes.ACONST_NULL);
        } else {
            return new LdcInsnNode(value);
        }
    }

    private static final String CLASS_DESCRIPTOR = "Ljava/lang/Class;";

    public static AbstractInsnNode createInsnNode(Type value) {
        if (value == null) {
            return new InsnNode(Opcodes.ACONST_NULL);
        } else {
            switch (value.getSort()) {
                case Type.BOOLEAN:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Boolean", "TYPE", CLASS_DESCRIPTOR);
                case Type.CHAR:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Character", "TYPE", CLASS_DESCRIPTOR);
                case Type.BYTE:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", CLASS_DESCRIPTOR);
                case Type.SHORT:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Short", "TYPE", CLASS_DESCRIPTOR);
                case Type.INT:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Integer", "TYPE", CLASS_DESCRIPTOR);
                case Type.FLOAT:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Float", "TYPE", CLASS_DESCRIPTOR);
                case Type.LONG:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Long", "TYPE", CLASS_DESCRIPTOR);
                case Type.DOUBLE:
                    return new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/Double", "TYPE", CLASS_DESCRIPTOR);
                default:
                    return new LdcInsnNode(value);
            }
        }
    }
}
