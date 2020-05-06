package com.microprofile.samples.services.book.auth;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilsTest {
    @Test
    void generateTokenString() {
        assertNotNull(TokenUtils.generateTokenString("admin", "admin"));
    }
}
