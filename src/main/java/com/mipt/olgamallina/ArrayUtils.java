package com.mipt.olgamallina;

public class ArrayUtils {

    public static <T> int findFirst(T[] array, T element) {
        if (array == null || array.length == 0) return -1;
        for (int i = 0; i < array.length; i++) {
            T item = array[i];
            if (item == element || (item != null && item.equals(element))) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        final String[] names = {"Alice", "Bob", "Charlie"};
        final int index = ArrayUtils.findFirst(names, "Bob");
        System.out.println(index);
        System.out.println(ArrayUtils.findFirst(names, "Zoe"));
        System.out.println(ArrayUtils.findFirst(new String[]{null, "x"}, null));
        System.out.println(ArrayUtils.findFirst(null, "x"));
    }
}