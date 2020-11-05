package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BtList extends BtParent {
    private final List<BtNode> children = new SmartList<>();
    private List<BtNode> unmodifiableChildren;

    @Override
    public Collection<BtNode> getChildren() {
        if (unmodifiableChildren == null) {
            unmodifiableChildren = Collections.unmodifiableList(children);
        }
        return unmodifiableChildren;
    }

    public BtNode get(int index) {
        return children.get(index);
    }

    public void add(BtNode node) {
        if (node != null) node.setParent(this);
        children.add(node);
    }

    public void addValue(Object value) {
        add(new BtValue(value));
    }

    public boolean remove(BtNode node) {
        return children.remove(node);
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public int size() {
        return children.size();
    }
}
