package com.mipt.olgamallina.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    private final PasswordEncoder delegate;
    private final String passwordSecret;

    public CustomPasswordEncoder(PasswordEncoder delegate, String passwordSecret) {
        this.delegate = delegate;
        this.passwordSecret = passwordSecret;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword + passwordSecret);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword + passwordSecret, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return delegate.upgradeEncoding(encodedPassword);
    }
}