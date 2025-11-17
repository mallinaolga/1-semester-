package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {

    @Test
    void shouldCacheResultOfFindDataByKey() {
        AtomicInteger calls = new AtomicInteger();

        DataService delegate = new DataService() {
            @Override
            public Optional<String> findDataByKey(String key) {
                calls.incrementAndGet();
                return Optional.of("value");
            }

            @Override
            public void saveData(String key, String data) {}

            @Override
            public boolean deleteData(String key) { return false; }
        };

        DataService caching = new CachingDecorator(delegate);

        Optional<String> v1 = caching.findDataByKey("k");
        Optional<String> v2 = caching.findDataByKey("k");

        assertEquals(Optional.of("value"), v1);
        assertEquals(Optional.of("value"), v2);
        assertEquals(1, calls.get(), "delegate.findDataByKey должен быть вызван только 1 раз");
    }

    @Test
    void shouldInvalidateCacheOnDelete() {
        SimpleDataService base = new SimpleDataService();
        DataService caching = new CachingDecorator(base);

        caching.saveData("k", "v1");
        caching.findDataByKey("k");
        caching.deleteData("k");

        assertTrue(caching.findDataByKey("k").isEmpty());
    }
}
