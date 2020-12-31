package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.Markers;
import com.github.mouse0w0.coffeemaker.template.TemplateParseException;
import com.github.mouse0w0.coffeemaker.template.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.tree.BtMethod;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtFieldInsn;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtMethodInsn;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapVarHandler extends MethodInsnHandler {
    @Override
    protected Method[] getAcceptableMethods() {
        return new Method[]{
                Utils.getDeclaredMethod(Markers.class, "$mapVar", String.class),
                Utils.getDeclaredMethod(Markers.class, "$mapClass", String.class, Class.class),
                Utils.getDeclaredMethod(Markers.class, "$mapStaticField", String.class, Object.class),
                Utils.getDeclaredMethod(Markers.class, "$mapEnd")};
    }

    private BtInsnNode startInsnNode;
    private Map<String, Object> map;

    @Override
    protected void handle(BtMethod method, BtInsnNode insn) {
        String name = insn.get(BtMethodInsn.NAME).getAsString();
        if ("$mapVar".equals(name)) {
            if (startInsnNode != null) throw new TemplateParseException("open map");
            startInsnNode = insn;
            map = new HashMap<>();
        } else if ("$mapClass".equals(name)) {
            if (startInsnNode == null) throw new TemplateParseException("isolated map class");
            BtInsnNode value = Utils.getPreviousConstant(insn, 0);
            BtInsnNode key = Utils.getPreviousConstant(insn, 1);
            map.put(key.getAsString(), value.getAsType());
        } else if ("$mapStaticField".equals(name)) {
            if (startInsnNode == null) throw new TemplateParseException("isolated map static field");
            BtInsnNode value = insn.getPrevious();
            BtInsnNode key = Utils.getPreviousConstant(insn, 1);
            map.put(key.getAsString(), new Field((BtFieldInsn) value));
        } else if ("$mapEnd".equals(name)) {
            if (startInsnNode == null) throw new TemplateParseException("isolated map end");
            BtInsnNode key = Utils.getPreviousConstant(startInsnNode, 0);
            BtClass clazz = (BtClass) method.getParent().getParent();
            clazz.getLocalVar().put(key.getAsString(), map);
            Utils.removeRange(method.getInstructions(), startInsnNode.getPreviousLabel(), insn.getNextLabel());
            startInsnNode = null;
            map = null;
        }
    }
}
