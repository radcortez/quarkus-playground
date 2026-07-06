package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.NumberConfig;
import com.radcortez.quarkus.playground.number.config.Prefix;
import io.quarkus.test.Mock;
import io.quarkus.test.component.QuarkusComponentTest;
import io.quarkus.test.component.TestConfigProperty;
import io.smallrye.config.SmallRyeConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusComponentTest
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

    @Inject
    NumberConfig numberConfig;

    @Test
    @TestConfigProperty(key = "number.generation.prefix", value = "UN")
    void prefix() {
        Mockito.when(numberConfig.generation().length()).thenReturn(3);
        String number = numberResource.generateNumber();
        assertNotNull(number);
        assertEquals(Prefix.UN, Prefix.valueOf(number.substring(0, 2)));
        assertEquals(6, number.length());
    }

    public static class AppConfigProducer {
        @Inject
        Config config;

        @Produces
        @Mock
        @Singleton
        NumberConfig numberConfig() {
            NumberConfig numberConfig = config.unwrap(SmallRyeConfig.class).getConfigMapping(NumberConfig.class);
            NumberConfig numberConfigSpy = Mockito.spy(numberConfig);
            NumberConfig.Generation generationSpy = Mockito.spy(numberConfigSpy.generation());
            Mockito.when(numberConfigSpy.generation()).thenReturn(generationSpy);
            return numberConfigSpy;
        }
    }
}
