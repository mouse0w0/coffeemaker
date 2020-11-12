package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import java.util.Collection;
import java.util.Iterator;

public abstract class BtParent<T extends BtNode> extends BtNode implements Iterable<T> {

    public abstract Collection<T> getChildren();

    public abstract boolean isEmpty();

    public abstract int size();

    @Override
    public Iterator<T> iterator() {
        return getChildren().iterator();
    }
}
