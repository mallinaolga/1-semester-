package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    void concurrentTransfers_areThreadSafe_andTotalBalancePreserved() throws Exception {
        Bank bank = new Bank();
        BankAccount a = new BankAccount(1, 10_000);
        BankAccount b = new BankAccount(2, 10_000);

        int threads = 30;
        int opsPerThread = 1_000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch readyLatch = new CountDownLatch(threads);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            futures.add(pool.submit(() -> {
                readyLatch.countDown();
                startLatch.await();
                for (int k = 0; k < opsPerThread; k++) {
                    if (idx % 2 == 0) {
                        bank.sendToAccount(a, b, 1);
                    } else {
                        bank.sendToAccount(b, a, 1);
                    }
                }
                return null;
            }));
        }

        readyLatch.await();
        startLatch.countDown();

        for (Future<?> f : futures) f.get();

        pool.shutdown();
        assertTrue(pool.awaitTermination(2, TimeUnit.SECONDS));

        assertEquals(20_000, a.getBalance() + b.getBalance());

        assertTrue(a.getBalance() >= 0);
        assertTrue(b.getBalance() >= 0);
    }


    @Test
    void concurrentTransfers_insufficientFunds_throwsAndDoesNotChangeBalance() throws Exception {
        Bank bank = new Bank();
        BankAccount from = new BankAccount(1, 50);
        BankAccount to   = new BankAccount(2, 0);

        int threads = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch readyLatch = new CountDownLatch(threads);
        AtomicInteger exceptions = new AtomicInteger();

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                readyLatch.countDown();
                startLatch.await();
                try {
                    bank.sendToAccount(from, to, 100);
                } catch (IllegalArgumentException e) {
                    exceptions.incrementAndGet();
                }
                return null;
            }));
        }

        readyLatch.await();
        startLatch.countDown();

        for (Future<?> f : futures) f.get();

        pool.shutdown();
        assertTrue(pool.awaitTermination(1, TimeUnit.SECONDS));

        assertEquals(threads, exceptions.get());

        assertEquals(50, from.getBalance());
        assertEquals(0,  to.getBalance());
    }


    @Test
    void sendToAccount_negativeAmount_throws() {
        Bank bank = new Bank();
        BankAccount a = new BankAccount(1, 100);
        BankAccount b = new BankAccount(2, 100);

        assertThrows(IllegalArgumentException.class,
                () -> bank.sendToAccount(a, b, -10));

        assertThrows(IllegalArgumentException.class,
                () -> bank.sendToAccount(a, b, 0));
    }


    @Test
    void deadlockMethod_canDeadlock_butTestDoesNotHang() throws InterruptedException {
        Bank bank = new Bank();
        BankAccount a = new BankAccount(1, 1_000);
        BankAccount b = new BankAccount(2, 1_000);

        CountDownLatch firstLocksTaken = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (a) {
                firstLocksTaken.countDown();
                try { firstLocksTaken.await(); } catch (InterruptedException ignored) {}
                bank.sendToAccountDeadlock(a, b, 1);
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (b) {
                firstLocksTaken.countDown();
                try { firstLocksTaken.await(); } catch (InterruptedException ignored) {}
                bank.sendToAccountDeadlock(b, a, 1);
            }
        });

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        t1.join(300);
        t2.join(300);

        assertTrue(t1.isAlive() && t2.isAlive(),
                "Ожидали дедлок: потоки должны зависнуть");
    }
}
