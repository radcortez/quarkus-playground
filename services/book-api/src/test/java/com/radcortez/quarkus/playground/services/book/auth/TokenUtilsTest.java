package com.radcortez.quarkus.playground.services.book.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilsTest {
    @Test
    void generateTokenString() {
        assertNotNull(TokenUtils.generateTokenString("admin", "admin"));
    }
}
