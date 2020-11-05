package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import java.util.Collection;
import java.util.Iterator;

public abstract class BtParent extends BtNode implements Iterable<BtNode> {

    public abstract Collection<BtNode> getChildren();

    public abstract boolean isEmpty();

    public abstract int size();

    @Override
    public Iterator<BtNode> iterator() {
        return getChildren().iterator();
    }
}
