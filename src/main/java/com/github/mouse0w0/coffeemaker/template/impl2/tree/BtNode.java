package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.EmptyEvaluator;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Type;

import java.util.Collection;

/**
 * Bytecode tree node
 */
public abstract class BtNode {

    private BtParent parent;

    public BtParent getParent() {
        return parent;
    }

    void setParent(BtParent parent) {
        this.parent = parent;
    }

    public Object get() {
        return compute(EmptyEvaluator.INSTANCE);
    }

    public int getAsInt() {
        return computeInt(EmptyEvaluator.INSTANCE);
    }

    public String getAsString() {
        return computeString(EmptyEvaluator.INSTANCE);
    }

    public boolean getAsBoolean() {
        return computeBoolean(EmptyEvaluator.INSTANCE);
    }

    public Object compute(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }

    public int computeInt(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new TemplateProcessException("The type of value is not Integer");
    }

    public String computeString(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        throw new TemplateProcessException("The type of value is not String");
    }

    public String computeInternalName(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        if (value instanceof Type) return ((Type) value).getInternalName();
        throw new TemplateProcessException("The type of value is not String");
    }

    public String computeDescriptor(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        if (value instanceof Type) return ((Type) value).getDescriptor();
        throw new TemplateProcessException("The type of value is not String");
    }

    public boolean computeBoolean(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new TemplateProcessException("The type of value is not Boolean");
    }

    @SuppressWarnings("unchecked")
    public String[] computeStringArray(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        else if (value instanceof Collection) return ((Collection<String>) value).toArray(new String[0]);
        else if (value instanceof String[]) return (String[]) value;
        throw new TemplateProcessException("The type of value is not String[]");
    }

    public Attribute computeAttribute(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value instanceof Attribute) return (Attribute) value;
        throw new TemplateProcessException("The type of value is not Attribute");
    }
}
