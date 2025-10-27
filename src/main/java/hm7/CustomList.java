package hm7;

import java.util.Iterator;

/**
 *
 * @param <T> тип элементов списка
 */

public interface CustomList <T> extends Iterable<T> {
    /**
     *
     * @param value элемент
     * @return true
     * @throws NullPointerException if value==null
     */

    boolean add(T value);

    /**
     *
     * @param index
     * @return element
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона
     */
    T get(int index);

    /**
     *
     * @param index 0...size-1
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона
     */
    T remove(int index);

    /**
     *
     * @return количество элементов
     */
    int size();

    /**
     *
     * @return true if size==0
     */
    default boolean isEmpty() {
        return size() == 0;
    }
    @Override
    Iterator<T> iterator();
}

