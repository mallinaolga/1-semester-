package hm7;

import java.util.Arrays;

/**
 *
 * @param <T> тип элементов
 */
public class CustomArrayList<T> implements CustomList<T>{
    private Object[] a = new Object[10];
    private int n = 0;

    /**
     *
     * @param v добавляемый элемент
     * @return всегда {@code true}
     * @throws NullPointerException если {@code v==null}
     */

    @Override
    public boolean add(T v) {
        if (v == null) throw new NullPointerException();
        if (n == a.length) grow();
        a[n++] = v;
        return true;
    }

    /**
     *
     * @param i индекс элемента
     * @return элемент на позиции
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    @Override
    public T get(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked") T v = (T) a[i];
        return v;
    }

    /**
     *
     * @param i индекс удаляемого элемента, {@code 0 <= i <size()}
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона {@code [0,size())}
     */

    @Override
    public T remove(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked") T old = (T) a[i];

        for (int k = i; k < n - 1; k++) a[k] = a[k + 1];
        a[--n] = null;
        return old;
    }

    /**
     *
     * @return число элементов
     */

    @Override
    public int size() { return n; }

    private void grow() {
        int now = a.length;
        int newlen= (now == 0) ? 1: (int) Math.ceil(now*1.5);
        a= Arrays.copyOf(a,newlen);
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int cursor = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return cursor < n;
            }

            @Override
            public T next() {
                if (cursor >= n) throw new java.util.NoSuchElementException();
                @SuppressWarnings("unchecked") T v = (T) a[cursor];
                lastRet = cursor++;
                return v;
            }

            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                CustomArrayList.this.remove(lastRet);
                lastRet = -1;
            }
        };
    }
}
