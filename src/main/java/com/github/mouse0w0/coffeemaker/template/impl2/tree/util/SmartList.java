package com.github.mouse0w0.coffeemaker.template.impl2.tree.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.RandomAccess;

public class SmartList<E> extends AbstractList<E> implements RandomAccess {
    private Object e;
    private int size = 0;

    public SmartList() {
    }

    public SmartList(E element) {
        add(element);
    }

    @SafeVarargs
    public SmartList(E... elements) {
        int length = elements.length;
        if (length == 1) {
            add(elements[0]);
        } else if (length > 1) {
            ensureCapacity(length);
            System.arraycopy(elements, 0, e, 0, length);
            size = length;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkSize(index);
        return size == 1 ? (E) e : (E) ((Object[]) e)[index];
    }

    @Override
    public void add(int index, E element) {
        checkSizeForAdd(index);
        ensureCapacity(index + 1);
        if (size == 0) {
            e = element;
            size = 1;
        } else {
            Object[] elements = (Object[]) e;
            int movedCount = size - index;
            if (movedCount > 0) {
                System.arraycopy(elements, index, elements, index + 1, movedCount);
            }
            elements[index] = element;
            size++;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkSize(index);
        if (size == 1) {
            E old = (E) e;
            e = element;
            return old;
        } else {
            Object[] elements = (Object[]) element;
            E old = (E) elements[index];
            elements[index] = element;
            return old;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkSize(index);
        if (size == 1) {
            E removed = (E) e;
            size = 0;
            return removed;
        } else if (size == 2) {
            Object[] elements = (Object[]) e;
            E removed = (E) elements[index];
            e = elements[index ^ 0x1];
            size = 1;
            return removed;
        } else {
            Object[] elements = (Object[]) e;
            E removed = (E) elements[index];
            int movedCount = size - index - 1;
            if (movedCount > 0) {
                System.arraycopy(elements, index + 1, elements, index, movedCount);
            }
            elements[--size] = null;
            return removed;
        }
    }

    @Override
    public int indexOf(Object o) {
        if (size == 0) return -1;
        if (size == 1) {
            if (o == null) return e == null ? 0 : -1;
            else return o.equals(e) ? 0 : -1;
        }

        Object[] elements = (Object[]) e;
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (size == 0) return -1;
        if (size == 1) {
            if (o == null) return e == null ? 0 : -1;
            else return o.equals(e) ? 0 : -1;
        }

        Object[] elements = (Object[]) e;
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elements[i] == null) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public void clear() {
        e = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        if (size == 1) {
            array[0] = e;
        } else if (size > 1) {
            System.arraycopy(e, 0, array, 0, size);
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass(), size);
        }

        if (size == 1) {
            a[0] = (T) e;
        } else if (size > 1) {
            System.arraycopy(e, 0, a, 0, size);
        }
        return a;
    }

    private void ensureCapacity(int capacity) {
        if (capacity == 1) return;

        if (e instanceof Object[]) {
            Object[] elements = (Object[]) e;
            int length = elements.length;
            if (length < capacity) {
                e = new Object[length << 1 + length];
                System.arraycopy(elements, 0, e, 0, length);
            }
        } else {
            Object[] elements = new Object[capacity];
            elements[0] = e;
            e = elements;
        }
    }

    private void checkSize(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }

    private void checkSizeForAdd(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }
}
