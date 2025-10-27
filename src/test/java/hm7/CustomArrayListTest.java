package hm7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

    private CustomList<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomArrayList<>();
    }

    @Test
    void add_increasesSize_andReturnsTrue() {
        assertTrue(list.add("A"));
        assertTrue(list.add("B"));
        assertEquals(2, list.size());
    }

    @Test
    void add_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    void get_validIndex_returnsElement() {
        list.add("A");
        list.add("B");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void get_outOfBounds_throws() {
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void remove_validIndex_returnsOld_andShiftsLeft_noHolesInside() {
        list.add("A");
        list.add("B");
        list.add("C");
        // удаляем середину
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
        // сдвиг без «дыр»: теперь [A, C]
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    @Test
    void remove_outOfBounds_throws() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void size_and_isEmpty_work() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        list.add("A");
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void iterator_traversesAll_inOrder() {
        list.add("A");
        list.add("B");
        list.add("C");
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        assertEquals("ABC", sb.toString());
    }

    @Test
    void iterator_next_pastEnd_throwsNoSuchElement() {
        list.add("A");
        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iterator_remove_removesLastReturned_andShiftsCursorBack() {
        list.add("A");
        list.add("B");
        list.add("C");
        Iterator<String> it = list.iterator();

        assertEquals("A", it.next());
        it.remove(); // удалили "A": список стал [B, C]
        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("C", list.get(1));

        // продолжаем итерацию: должны увидеть "C"
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iterator_remove_withoutNext_illegalState() {
        list.add("A");
        Iterator<String> it = list.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void grow_beyondInitialCapacity_works() {
        // начальная ёмкость 10: добавим 15
        for (int i = 0; i < 15; i++) {
            assertTrue(list.add("V" + i));
        }
        assertEquals(15, list.size());
        // проверим несколько элементов
        assertEquals("V0", list.get(0));
        assertEquals("V9", list.get(9));
        assertEquals("V14", list.get(14));
    }
}
