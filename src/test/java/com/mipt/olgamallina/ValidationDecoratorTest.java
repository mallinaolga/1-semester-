package com.mipt.olgamallina;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {

    @Test
    void shouldThrowOnEmptyKeyInSave() {
        DataService base = new SimpleDataService();
        DataService validation = new ValidationDecorator(base);

        assertThrows(IllegalArgumentException.class,
                () -> validation.saveData("   ", "data"));
    }

    @Test
    void shouldThrowOnNullDataInSave() {
        DataService base = new SimpleDataService();
        DataService validation = new ValidationDecorator(base);

        assertThrows(IllegalArgumentException.class,
                () -> validation.saveData("key", null));
    }

    @Test
    void shouldPassValidDataThrough() {
        DataService base = new SimpleDataService();
        DataService validation = new ValidationDecorator(base);

        validation.saveData("key", "value");

        assertEquals("value",
                validation.findDataByKey("key").orElse(null));
    }

    @Test
    void shouldThrowOnBlankKeyInFind() {
        DataService base = new SimpleDataService();
        DataService validation = new ValidationDecorator(base);

        assertThrows(IllegalArgumentException.class,
                () -> validation.findDataByKey(""));
    }

    @Test
    void shouldThrowOnBlankKeyInDelete() {
        DataService base = new SimpleDataService();
        DataService validation = new ValidationDecorator(base);

        assertThrows(IllegalArgumentException.class,
                () -> validation.deleteData("   "));
    }
}
