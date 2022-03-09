package com.radcortez.quarkus.playground.services.book.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthCredentials {
    private static final char SEMI_COLON = ':';
    private static final int BASIC_AUTH_PREFIX_LENGTH = "Basic ".length();

    private String username;
    private String password;

    private static class NullAuthCredentials extends BasicAuthCredentials {

        private NullAuthCredentials() {
            super(null, null);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isNull() {
            return true;
        }
    }

    private static final BasicAuthCredentials CREDENTIALS_NULL = new NullAuthCredentials();
    private static final BasicAuthCredentials CREDENTIALS_INVALID = new BasicAuthCredentials(null, null);

    public static BasicAuthCredentials createCredentialsFromHeader(final String authorizationHeader) {
        if (authorizationHeader == null) {
            return CREDENTIALS_NULL;
        }

        if (authorizationHeader.length() < BASIC_AUTH_PREFIX_LENGTH) {
            return CREDENTIALS_INVALID;
        }

        String authPart = authorizationHeader.substring(BASIC_AUTH_PREFIX_LENGTH);
        String userpass;
        userpass = new String(Base64.getDecoder().decode((authPart.getBytes(StandardCharsets.UTF_8))));
        int index = userpass.indexOf(SEMI_COLON);
        if (index < 1) {
            return CREDENTIALS_INVALID;
        }
        String name = userpass.substring(0, index);
        String pass = userpass.substring(index + 1);
        return new BasicAuthCredentials(name, pass);
    }

    public BasicAuthCredentials(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public boolean isValid() {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }

    public boolean isNull() {
        return false;
    }

    /**
     * Get the username.
     *
     * @return the username or null if the username was not found
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password.
     *
     * @return the password or null if the password was not found
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserPassCredentials [username=" + username + "]";
    }
}
