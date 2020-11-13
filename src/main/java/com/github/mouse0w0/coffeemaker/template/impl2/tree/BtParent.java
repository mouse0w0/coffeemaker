package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import java.util.Collection;

public abstract class BtParent<E extends BtNode> extends BtNode implements Iterable<E> {

    public abstract Collection<E> getChildren();

    public abstract boolean isEmpty();

    public abstract int size();
}
