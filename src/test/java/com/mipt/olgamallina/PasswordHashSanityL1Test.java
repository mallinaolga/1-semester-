package com.mipt.olgamallina;

import com.mipt.olgamallina.security.CustomPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashSanityL1Test {

    @Test
    void passwordShouldBeEncodedAndMatchedWithSecret() {
        CustomPasswordEncoder encoder =
                new CustomPasswordEncoder(new BCryptPasswordEncoder(12), "test-secret");

        String hash = encoder.encode("password");

        assertNotEquals("password", hash);
        assertTrue(encoder.matches("password", hash));
        assertFalse(encoder.matches("wrong", hash));
    }
}