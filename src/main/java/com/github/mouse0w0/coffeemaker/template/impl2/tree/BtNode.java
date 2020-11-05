package com.github.mouse0w0.coffeemaker.template.impl2.tree;

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
}
