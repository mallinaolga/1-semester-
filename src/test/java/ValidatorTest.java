package com.mipt.hm8;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    static class User {
        @NotNull(message = "Имя не может быть null")
        @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
        private String name;

        @Email(message = "Некорректный формат email")
        @NotNull(message = "Email не может быть null")
        private String email;

        @Range(min = 0, max = 150, message = "Возраст должен быть от 0 до 150")
        private Integer age;

        @Size(min = 6, max = 20, message = "Пароль должен быть от 6 до 20 символов")
        private String password;

        User(String name, String email, Integer age, String password) {
            this.name = name; this.email = email; this.age = age; this.password = password;
        }
    }

    @Test
    void validObject_passes() {
        User u = new User("Alice", "alice@example.com", 25, "secret12");
        ValidationResult r = Validator.validate(u);
        assertTrue(r.isValid(), "Ожидали валидный объект");
        assertEquals(0, r.getErrors().size());
    }

    @Test
    void size_and_email_fail() {
        User u = new User("A", "bad@", 20, "123");
        ValidationResult r = Validator.validate(u);
        assertFalse(r.isValid());
        List<String> errs = r.getErrors();
        assertTrue(errs.stream().anyMatch(s -> s.contains("name") && s.contains("2 до 50")));
        assertTrue(errs.stream().anyMatch(s -> s.contains("email") && s.contains("Некорректный формат")));
        assertTrue(errs.stream().anyMatch(s -> s.contains("password") && s.contains("6 до 20")));
    }


    @Test
    void range_out_of_bounds_fails() {
        User u = new User("Bob", "bob@example.com", 200, "password");
        ValidationResult r = Validator.validate(u);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("age") && s.contains("0 до 150")));
    }

    @Test
    void notNull_on_email_fails_when_null() {
        User u = new User("Bob", null, 33, "password");
        ValidationResult r = Validator.validate(u);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("email") && s.contains("не может быть null")));
        long emailErrors = r.getErrors().stream().filter(s -> s.contains("email")).count();
        assertEquals(1, emailErrors);
    }


    @Test
    void size_on_bounds_ok() {
        User u1 = new User("Al", "a@b.co", 10, "123456");
        User u2 = new User("X".repeat(50), "x@x.xx", 150, "Y".repeat(20));
        assertTrue(Validator.validate(u1).isValid());
        assertTrue(Validator.validate(u2).isValid());
    }

    @Test
    void range_on_bounds_ok() {
        User uMin = new User("Ok", "ok@ok.ok", 0, "123456");
        User uMax = new User("Ok", "ok@ok.ok", 150, "123456");
        assertTrue(Validator.validate(uMin).isValid());
        assertTrue(Validator.validate(uMax).isValid());
    }

    @Test
    void null_root_object_fails() {
        ValidationResult r = Validator.validate(null);
        assertFalse(r.isValid());
        assertTrue(r.getErrors().stream().anyMatch(s -> s.contains("object is null")));
    }
}
