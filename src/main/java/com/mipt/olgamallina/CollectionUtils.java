package com.mipt.olgamallina;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionUtils {
    public static <T> List<T> mergeLists(List<? extends T> list1,
                                         List<? extends T> list2) {
        int cap = (list1 == null ? 0 : list1.size()) + (list2 == null ? 0 : list2.size());
        List<T> result = new ArrayList<>(cap);
        if (list1 != null) result.addAll(list1);
        if (list2 != null) result.addAll(list2);
        return result;
    }
    public static <T> void addAll(List<? super T> destination,
                                  List<? extends T> source) {
        Objects.requireNonNull(destination, "destination must not be null");
        if (source == null) return;
        destination.addAll(source);
    }

    public static void main(String[] args) {
        List<Integer> list1 = java.util.Arrays.asList(1, 2, 3);
        List<Double> list2 = java.util.Arrays.asList(4.5, 5.6);

        List<Number> merged = CollectionUtils.mergeLists(list1, list2);
        System.out.println(merged);
        List<Object> destination = new ArrayList<>();
        CollectionUtils.addAll(destination, list1);
        System.out.println(destination);
    }
}