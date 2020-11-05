package com.github.mouse0w0.coffeemaker.template.impl2.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BtObject extends BtParent {
    private final Map<String, BtNode> children = new HashMap<>();

    @SuppressWarnings("unchecked")
    public final <T extends BtNode> T get(String key) {
        return (T) children.get(key);
    }

    @SuppressWarnings("unchecked")
    public final <T extends BtNode> T getOrDefault(String key, T defaultValue) {
        return (T) children.getOrDefault(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public final <T extends BtNode> T computeIfNull(String key,
                                                    Function<? super String, T> mappingFunction) {
        T btNode = (T) children.get(key);
        if (btNode == null) {
            btNode = mappingFunction.apply(key);
            put(key, btNode);
        }
        return btNode;
    }

    public final BtNode put(String key, BtNode node) {
        if (node != null) node.setParent(this);
        return children.put(key, node);
    }

    public final BtNode putValue(String key, Object value) {
        return put(key, new BtValue(value));
    }

    public final BtNode remove(String key) {
        return children.remove(key);
    }

    public boolean containsKey(Object key) {
        return children.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return children.containsValue(value);
    }

    public void clear() {
        children.clear();
    }

    public Set<String> keySet() {
        return children.keySet();
    }

    public Collection<BtNode> values() {
        return children.values();
    }

    public Set<Map.Entry<String, BtNode>> entrySet() {
        return children.entrySet();
    }

    public void forEach(BiConsumer<? super String, ? super BtNode> action) {
        children.forEach(action);
    }

    @Override
    public final Collection<BtNode> getChildren() {
        return children.values();
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
