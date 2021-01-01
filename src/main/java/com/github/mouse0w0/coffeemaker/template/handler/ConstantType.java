package com.github.mouse0w0.coffeemaker.template.handler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public enum ConstantType {
    BOOL() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            methodVisitor.visitInsn((boolean) o ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        }
    },
    CHAR() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            int value = (char) o;
            if (value >= -1 && value <= 5) {
                methodVisitor.visitInsn(Opcodes.ICONST_0 + value);
            } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
                methodVisitor.visitIntInsn(Opcodes.BIPUSH, value);
            } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                methodVisitor.visitIntInsn(Opcodes.SIPUSH, value);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    INT() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            int value = ((Number) o).intValue();
            if (value >= -1 && value <= 5) {
                methodVisitor.visitInsn(Opcodes.ICONST_0 + value);
            } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
                methodVisitor.visitIntInsn(Opcodes.BIPUSH, value);
            } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                methodVisitor.visitIntInsn(Opcodes.SIPUSH, value);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    LONG() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            long value = (long) o;
            if (value == 0L || value == 1L) {
                methodVisitor.visitInsn(Opcodes.LCONST_0 + (int) value);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    FLOAT() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            float value = (float) o;
            int bits = Float.floatToIntBits(value);
            if (bits == 0L || bits == 0x3F800000 || bits == 0x40000000) { // 0..2
                methodVisitor.visitInsn(Opcodes.FCONST_0 + (int) value);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    DOUBLE() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            double value = (double) o;
            long bits = Double.doubleToLongBits(value);
            if (bits == 0L || bits == 0x3FF0000000000000L) { // +0.0d and 1.0d
                methodVisitor.visitInsn(Opcodes.DCONST_0 + (int) value);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    STRING() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            if (o == null) {
                methodVisitor.visitInsn(Opcodes.ACONST_NULL);
            } else {
                methodVisitor.visitLdcInsn(o);
            }
        }
    },
    CLASS() {
        @Override
        public void accept(MethodVisitor methodVisitor, Object o) {
            Type value = (Type) o;
            if (value == null) {
                methodVisitor.visitInsn(Opcodes.ACONST_NULL);
            } else {
                switch (value.getSort()) {
                    case Type.BOOLEAN:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Boolean", "TYPE", CLASS_DESCRIPTOR);
                    case Type.CHAR:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Character", "TYPE", CLASS_DESCRIPTOR);
                    case Type.BYTE:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", CLASS_DESCRIPTOR);
                    case Type.SHORT:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Short", "TYPE", CLASS_DESCRIPTOR);
                    case Type.INT:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Integer", "TYPE", CLASS_DESCRIPTOR);
                    case Type.FLOAT:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Float", "TYPE", CLASS_DESCRIPTOR);
                    case Type.LONG:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Long", "TYPE", CLASS_DESCRIPTOR);
                    case Type.DOUBLE:
                        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Double", "TYPE", CLASS_DESCRIPTOR);
                    default:
                        methodVisitor.visitLdcInsn(o);
                }
            }
        }
    };

    private static final String CLASS_DESCRIPTOR = "Ljava/lang/Class;";

    public abstract void accept(MethodVisitor methodVisitor, Object o);
}
