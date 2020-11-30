package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.EmptyEvaluator;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Type;

import java.util.Collection;
import java.util.regex.Pattern;

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

    public Object computeNonNull(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }

    public int computeInt(Evaluator evaluator) {
        Object value = computeNonNull(evaluator);
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new TemplateProcessException("The type of value is not Integer, " + toString());
    }

    public String computeString(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        throw new TemplateProcessException("The type of value is not String, " + toString());
    }

    public String computeInternalName(Evaluator evaluator) {
        Object value = computeNonNull(evaluator);
        if (value == null) return null;
        if (value instanceof String) return toInternalName((String) value);
        if (value instanceof Type) return ((Type) value).getInternalName();
        throw new TemplateProcessException("The type of value is not String or Type, " + toString());
    }

    public String computeDescriptor(Evaluator evaluator) {
        Object value = computeNonNull(evaluator);
        if (value instanceof String) return toDescriptor((String) value);
        if (value instanceof Type) return ((Type) value).getDescriptor();
        throw new TemplateProcessException("The type of value is not String or Type, " + toString());
    }

    private static final Pattern INTERNAL_NAME = Pattern.compile("^[a-zA-Z$_][0-9a-zA-Z$_]*(/[a-zA-Z$_][0-9a-zA-Z$_]*)*");
    private static final Pattern DESCRIPTOR = Pattern.compile("^[VZCBSIFJD\\[L(].*");

    private static String toInternalName(String value) {
        return INTERNAL_NAME.matcher(value).matches() ? value : Type.getType(value).getInternalName();
    }

    private static String toDescriptor(String value) {
        return DESCRIPTOR.matcher(value).matches() ? value : Type.getObjectType(value).getDescriptor();
    }

    public boolean computeBoolean(Evaluator evaluator) {
        Object value = computeNonNull(evaluator);
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new TemplateProcessException("The type of value is not Boolean, " + toString());
    }

    @SuppressWarnings("unchecked")
    public String[] computeStringArray(Evaluator evaluator) {
        Object value = compute(evaluator);
        if (value == null) return null;
        else if (value instanceof Collection) return ((Collection<String>) value).toArray(new String[0]);
        else if (value instanceof String[]) return (String[]) value;
        throw new TemplateProcessException("The type of value is not String[], " + toString());
    }

    public Attribute computeAttribute(Evaluator evaluator) {
        Object value = computeNonNull(evaluator);
        if (value instanceof Attribute) return (Attribute) value;
        throw new TemplateProcessException("The type of value is not Attribute, " + toString());
    }
}
