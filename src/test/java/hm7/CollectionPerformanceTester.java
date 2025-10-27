package hm7;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class CollectionPerformanceTester {
    private static final int N = 10000;
    private static final int REPEATS = 5;
    private static final int WARMUP = 2;
    private static final int QUERIES = 50_000;

    @Test
    void compareArrayListAndLinkedList() {
        runSuite("add at end",        this::benchAddAtEnd);        // (3) добавление в конец
        runSuite("add at beginning",  this::benchAddAtBeginning);  // (4) добавление в начало
        runSuite("insert into middle",this::benchInsertMiddle);    // (5) вставка в середину
        runSuite("get by index",      this::benchGetByIndex);      // (6) доступ по индексу
        runSuite("remove from beginning", this::benchRemoveFromBeginning); // (7) удаление из начала
        runSuite("remove from end",      this::benchRemoveFromEnd);       // (8) удаление из конца
    }

    private interface Op { long run(List<Integer> list); }

    private void runSuite(String title, Op op) {
        System.out.println("\n=== " + title + " ===");
        long al = runWith(new ArrayList<>(), op);
        long ll = runWith(new LinkedList<>(), op);
        System.out.printf(Locale.ROOT, "ArrayList : %,d ns%n", al);
        System.out.printf(Locale.ROOT, "LinkedList: %,d ns%n", ll);
        System.out.printf(Locale.ROOT, "Faster    : %s%n", (al < ll ? "ArrayList" : "LinkedList"));
    }

    private long runWith(List<Integer> list, Op op) {
        for (int i = 0; i < WARMUP; i++) { list.clear(); prepare(list); op.run(list); }
        long sum = 0;
        for (int i = 0; i < REPEATS; i++) { list.clear(); prepare(list); sum += op.run(list); }
        return sum / REPEATS;
    }

    private void prepare(List<Integer> list) { list.addAll(range(N)); }
    private List<Integer> range(int n) { ArrayList<Integer> r = new ArrayList<>(n); for (int i=0;i<n;i++) r.add(i); return r; }

    private long benchAddAtEnd(List<Integer> list) { list.clear(); long t0=System.nanoTime(); for (int i=0;i<N;i++) list.add(i); return System.nanoTime()-t0; }
    private long benchAddAtBeginning(List<Integer> list) { list.clear(); long t0=System.nanoTime(); for (int i=0;i<N;i++) list.add(0,i); return System.nanoTime()-t0; }
    private long benchInsertMiddle(List<Integer> list) { list.clear(); long t0=System.nanoTime(); for (int i=0;i<N;i++) list.add(list.size()/2, i); return System.nanoTime()-t0; }
    private long benchGetByIndex(List<Integer> list) { prepare(list); long t0=System.nanoTime(); long s=0; for (int i=0;i<N;i++) s+= list.get(i); long t=System.nanoTime()-t0; if (s==42) System.out.print(""); return t; }
    private long benchRemoveFromBeginning(List<Integer> list) { int ops=Math.min(QUERIES,list.size()); long t0=System.nanoTime(); for (int i=0;i<ops;i++) list.remove(0); return System.nanoTime()-t0; }
    private long benchRemoveFromEnd(List<Integer> list) { int ops=Math.min(QUERIES,list.size()); long t0=System.nanoTime(); for (int i=0;i<ops;i++) list.remove(list.size()-1); return System.nanoTime()-t0; }


    private int[] randomIndexes(int bound, int count) { ThreadLocalRandom rnd=ThreadLocalRandom.current(); int[] arr=new int[count]; for (int i=0;i<count;i++) arr[i]=rnd.nextInt(bound); return arr; }
}
