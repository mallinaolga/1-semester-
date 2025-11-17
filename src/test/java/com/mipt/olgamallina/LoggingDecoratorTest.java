package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {

    @Test
    void shouldDelegateCallsToUnderlyingService() {
        SimpleDataService base = new SimpleDataService();
        DataService logging = new LoggingDecorator(base);

        logging.saveData("k", "v");
        assertEquals("v", logging.findDataByKey("k").orElse(null));

        assertTrue(logging.deleteData("k"));
        assertTrue(logging.findDataByKey("k").isEmpty());
    }
}
