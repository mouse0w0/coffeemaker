package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.TemplateParseException;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;

import java.lang.reflect.Method;

public class ConstantsHandler extends MethodInsnHandler {

    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{
                Utils.getDeclaredMethod(Markers.class, "$int", String.class),
                Utils.getDeclaredMethod(Markers.class, "$long", String.class),
                Utils.getDeclaredMethod(Markers.class, "$float", String.class),
                Utils.getDeclaredMethod(Markers.class, "$double", String.class),
                Utils.getDeclaredMethod(Markers.class, "$string", String.class),
                Utils.getDeclaredMethod(Markers.class, "$class", String.class),
                Utils.getDeclaredMethod(Markers.class, "$bool", String.class),
                Utils.getDeclaredMethod(Markers.class, "$byte", String.class),
                Utils.getDeclaredMethod(Markers.class, "$short", String.class),
                Utils.getDeclaredMethod(Markers.class, "$char", String.class)
        };
    }

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        BtInsnList instructions = method.getInstructions();
        BtInsnNode arg0 = Utils.getPreviousConstant(insn, 0);
        ConstantType type = getConstantType(insn.get(BtMethodInsn.NAME).getAsString());
        instructions.insert(insn, new BtComputableConstant(type, arg0.getAsString()));
        instructions.remove(arg0);
        instructions.remove(insn);
    }

    private static ConstantType getConstantType(String method) {
        switch (method) {
            case "$bool":
                return ConstantType.BOOL;
            case "$char":
                return ConstantType.CHAR;
            case "$int":
            case "$short":
            case "$byte":
                return ConstantType.INT;
            case "$long":
                return ConstantType.LONG;
            case "$float":
                return ConstantType.FLOAT;
            case "$double":
                return ConstantType.DOUBLE;
            case "$string":
                return ConstantType.STRING;
            case "$class":
                return ConstantType.CLASS;
            default:
                throw new TemplateParseException("Should not reach here");
        }
    }
}
