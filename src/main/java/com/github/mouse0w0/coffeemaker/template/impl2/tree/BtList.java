package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import com.github.mouse0w0.coffeemaker.template.impl2.tree.util.SmartList;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BtList<T extends BtNode> extends BtParent<T> {
    private final List<T> children = new SmartList<>();
    private List<T> unmodifiableChildren;

    @Override
    public Collection<T> getChildren() {
        if (unmodifiableChildren == null) {
            unmodifiableChildren = Collections.unmodifiableList(children);
        }
        return unmodifiableChildren;
    }

    public T get(int index) {
        return children.get(index);
    }

    public void add(T node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node.setParent(this);
        children.add(node);
    }

    @SuppressWarnings("unchecked")
    public void addValue(Object value) {
        add((T) new BtValue(value));
    }

    public boolean remove(Object o) {
        return children.remove(o);
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public Iterator<T> iterator() {
        return children.iterator();
    }
}
