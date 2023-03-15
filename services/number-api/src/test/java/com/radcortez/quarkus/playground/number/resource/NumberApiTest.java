package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.Prefix;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
public class NumberApiTest {
    @Inject
    NumberResource numberResource;

    @Test
    void generate() {
        String number = numberResource.generateNumber();
        assertNotNull(number);
        assertDoesNotThrow(() -> Prefix.valueOf(number.substring(0, 2)));
        assertEquals("-", number.substring(2, 3));
        assertTrue(Integer.parseInt(number.substring(3)) > 0);
        assertTrue(Integer.parseInt(number.substring(3)) < 99999999);
    }
}
