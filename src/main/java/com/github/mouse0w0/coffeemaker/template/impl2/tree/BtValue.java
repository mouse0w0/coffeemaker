package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import org.objectweb.asm.Attribute;

import java.util.Collection;

public class BtValue extends BtNode {

    private final Object value;

    public BtValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object compute(Evaluator evaluator) {
        return value;
    }

    @Override
    public int computeInt(Evaluator evaluator) {
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new TemplateProcessException("The type of value is not Integer");
    }

    @Override
    public boolean computeBoolean(Evaluator evaluator) {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new TemplateProcessException("The type of value is not Boolean");
    }

    @Override
    public String computeString(Evaluator evaluator) {
        if (value == null) return null;
        if (value instanceof String) {
            return (String) value;
        }
        throw new TemplateProcessException("The type of value is not String");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String[] computeStringArray(Evaluator evaluator) {
        if (value instanceof Collection) {
            return ((Collection<String>) value).toArray(new String[0]);
        } else if (value instanceof String[]) {
            return (String[]) value;
        } else if (value == null) {
            return null;
        }
        throw new TemplateProcessException("The type of value is not String[]");
    }

    @Override
    public Attribute computeAttribute(Evaluator evaluator) {
        if (value instanceof Attribute) {
            return (Attribute) value;
        }
        throw new TemplateProcessException("The type of value is not Attribute");
    }
}
