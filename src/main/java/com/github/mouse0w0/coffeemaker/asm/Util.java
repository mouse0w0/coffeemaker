package com.github.mouse0w0.coffeemaker.asm;

import java.util.ArrayList;
import java.util.List;

final class Util {
    static <T> List<T> add(final List<T> list, final T element) {
        List<T> newList = list == null ? new ArrayList<>(1) : list;
        newList.add(element);
        return newList;
    }
}
