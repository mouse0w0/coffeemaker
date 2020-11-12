package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.evaluator.EmptyEvaluator;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import org.objectweb.asm.Attribute;

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
        throw new UnsupportedOperationException();
    }

    public String computeString(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }

    public boolean computeBoolean(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }

    public String[] computeStringArray(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }

    public Attribute computeAttribute(Evaluator evaluator) {
        throw new UnsupportedOperationException();
    }
}
