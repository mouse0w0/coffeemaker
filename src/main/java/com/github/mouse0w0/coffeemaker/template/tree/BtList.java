package com.github.mouse0w0.coffeemaker.template.tree;

import com.github.mouse0w0.coffeemaker.template.tree.util.SmartList;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BtList<E extends BtNode> extends BtParent<E> {
    private final List<E> children = new SmartList<>();
    private List<E> unmodifiableChildren;

    @Override
    public Collection<E> getChildren() {
        if (unmodifiableChildren == null) {
            unmodifiableChildren = Collections.unmodifiableList(children);
        }
        return unmodifiableChildren;
    }

    public E get(int index) {
        return children.get(index);
    }

    public void add(E node) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        node.setParent(this);
        children.add(node);
    }

    @SuppressWarnings("unchecked")
    public void addValue(Object value) {
        add((E) new BtValue(value));
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

    public Object[] toArray() {
        return children.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return children.toArray(a);
    }

    @Override
    public Iterator<E> iterator() {
        return children.iterator();
    }
}
