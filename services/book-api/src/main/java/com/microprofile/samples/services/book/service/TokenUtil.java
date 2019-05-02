package com.microprofile.samples.services.book.service;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.eclipse.microprofile.jwt.Claims;

/**
 * Utilities for generating a JWT for testing
 */
public class TokenUtil {

    public static final String PRIVATE_KEY_PEM = "/privateKey-pkcs8.pem";

    private TokenUtil() {
    }

    /**
     * Utility method to generate a JWT string from a JSON resource file that is signed by the privateKey-pkcs1.pem
     * test resource key.
     *
     * @param jsonResName - name of test resources file
     * @return the JWT string
     * @throws Exception on parse failure
     */
    public static String generateTokenString(String jsonResName) throws Exception {
        return generateTokenString(jsonResName, Collections.emptySet());
    }

    /**
     * Utility method to generate a JWT string from a JSON resource file that is signed by the privateKey-pkcs1.pem
     * test resource key, possibly with invalid fields.
     *
     * @param jsonResName   - name of test resources file
     * @param invalidClaims - the set of claims that should be added with invalid values to test failure modes
     * @return the JWT string
     * @throws Exception on parse failure
     */
    public static String generateTokenString(String jsonResName, Set<InvalidClaims> invalidClaims) throws Exception {
        return generateTokenString(jsonResName, invalidClaims, null);
    }


    public static JSONObject of(final String content) throws Exception {
        final JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        return (JSONObject) parser.parse(content);
    }

    /**
     * Utility method to generate a JWT string from a JSON resource file that is signed by the privateKey-pkcs1.pem
     * test resource key, possibly with invalid fields.
     *
     * @param jsonResName   - name of test resources file
     * @param invalidClaims - the set of claims that should be added with invalid values to test failure modes
     * @param timeClaims    - used to return the exp, iat, auth_time claims
     * @return the JWT string
     * @throws Exception on parse failure
     */
    public static String generateTokenString(String jsonResName, Set<InvalidClaims> invalidClaims, Map<String, Long> timeClaims) throws Exception {
        if (invalidClaims == null) {
            invalidClaims = Collections.emptySet();
        }
        final InputStream contentIS = TokenUtil.class.getResourceAsStream(jsonResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        byte[] content = new byte[length];
        System.arraycopy(tmp, 0, content, 0, length);

        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject jwtContent = (JSONObject) parser.parse(content);

        return generateTokenString(jwtContent, invalidClaims, timeClaims);
    }

    /**
     * Utility method to generate a JWT string from a JSON resource file that is signed by the privateKey-pkcs1.pem
     * test resource key, possibly with invalid fields.
     *
     * @param jwtContent   - the JSON Payload for the JWT
     * @param invalidClaims - the set of claims that should be added with invalid values to test failure modes
     * @param timeClaims    - used to return the exp, iat, auth_time claims
     * @return the JWT string
     * @throws Exception on parse failure
     */
    public static String generateTokenString(JSONObject jwtContent, Set<InvalidClaims> invalidClaims, Map<String, Long> timeClaims) throws Exception {
        if (invalidClaims == null) {
            invalidClaims = Collections.emptySet();
        }

        // Change the issuer to INVALID_ISSUER for failure testing if requested
        if (invalidClaims.contains(InvalidClaims.ISSUER)) {
            jwtContent.put(Claims.iss.name(), "INVALID_ISSUER");
        }

        long currentTimeInSecs = currentTimeInSecs();
        long exp = currentTimeInSecs + 300;

        // Check for an input exp to override the default of now + 300 seconds
        if (timeClaims != null && timeClaims.containsKey(Claims.exp.name())) {
            exp = timeClaims.get(Claims.exp.name());
        }
        jwtContent.put(Claims.iat.name(), currentTimeInSecs);
        jwtContent.put(Claims.auth_time.name(), currentTimeInSecs);

        // If the exp claim is not updated, it will be an old value that should be seen as expired
        if (!invalidClaims.contains(InvalidClaims.EXP)) {
            jwtContent.put(Claims.exp.name(), exp);
        }
        if (timeClaims != null) {
            timeClaims.put(Claims.iat.name(), currentTimeInSecs);
            timeClaims.put(Claims.auth_time.name(), currentTimeInSecs);
            timeClaims.put(Claims.exp.name(), exp);
        }

        PrivateKey pk;
        if (invalidClaims.contains(InvalidClaims.SIGNER)) {
            // Generate a new random private key to sign with to test invalid signatures
            KeyPair keyPair = generateKeyPair(2048);
            pk = keyPair.getPrivate();

        } else {

            // Use the test private key associated with the test public key for a valid signature
            pk = readPrivateKey(PRIVATE_KEY_PEM);
        }

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(pk);
        final JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwtContent);
        JWSAlgorithm alg = JWSAlgorithm.RS256;

        if (invalidClaims.contains(InvalidClaims.ALG)) {
            alg = JWSAlgorithm.HS256;
            final SecureRandom random = new SecureRandom();
            final BigInteger secret = BigInteger.probablePrime(256, random);
            signer = new MACSigner(secret.toByteArray());
        }

        final JWSHeader jwtHeader = new JWSHeader.Builder(alg)
                .keyID(PRIVATE_KEY_PEM)
                .type(JOSEObjectType.JWT)
                .build();

        final SignedJWT signedJWT = new SignedJWT(jwtHeader, claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    public static PrivateKey readPrivateKey(String pemResName) throws Exception {
        InputStream contentIS = TokenUtil.class.getResourceAsStream(pemResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length));
    }

    /**
     * Read a PEM encoded public key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PublicKey
     * @throws Exception on decode failure
     */
    public static PublicKey readPublicKey(String pemResName) throws Exception {
        InputStream contentIS = TokenUtil.class.getResourceAsStream(pemResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePublicKey(new String(tmp, 0, length));
    }

    /**
     * Generate a new RSA keypair.
     *
     * @param keySize - the size of the key
     * @return KeyPair
     * @throws NoSuchAlgorithmException on failure to load RSA key generator
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    public static PrivateKey decodePrivateKey(String pemEncoded) throws Exception {
        pemEncoded = removeBeginEnd(pemEncoded);
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pemEncoded);

        // extract the private key

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    /**
     * Decode a PEM encoded public key string to an RSA PublicKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PublicKey
     * @throws Exception on decode failure
     */
    public static PublicKey decodePublicKey(String pemEncoded) throws Exception {
        pemEncoded = removeBeginEnd(pemEncoded);
        byte[] encodedBytes = Base64.getDecoder().decode(pemEncoded);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    /**
     * @return the current time in seconds since epoch
     */
    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

    /**
     * Enums to indicate which claims should be set to invalid values for testing failure modes
     */
    public enum InvalidClaims {
        ISSUER, // Set an invalid issuer
        EXP,    // Set an invalid expiration
        SIGNER, // Sign the token with the incorrect private key
        ALG, // Sign the token with the correct private key, but HS
    }
}
