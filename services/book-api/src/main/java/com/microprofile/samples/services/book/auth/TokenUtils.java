package com.microprofile.samples.services.book.auth;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Set;

/**
 * Utilities for generating a JWT for testing
 */
public class TokenUtils {
    private TokenUtils() {
        // no-op: utility class
    }

    public static String generateTokenString(final String username, final String... groups) {
        return generateTokenString(username, Set.of(groups));
    }

    public static String generateTokenString(final String username, final Set<String> groups) {
        long currentTimeInSecs = currentTimeInSecs();

        return Jwt.claims()
                  .issuer("https://konoha.books.com")
                  .issuedAt(currentTimeInSecs())
                  .claim(Claims.auth_time.name(), currentTimeInSecs)
                  .expiresAt(currentTimeInSecs + 1800)
                  .upn(username)
                  .preferredUserName(username)
                  .groups(groups)
                  .sign();
    }

    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }
}
