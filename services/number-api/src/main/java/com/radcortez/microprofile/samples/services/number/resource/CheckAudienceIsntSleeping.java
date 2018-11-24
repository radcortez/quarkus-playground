package com.radcortez.microprofile.samples.services.number.resource;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Health
@ApplicationScoped
public class CheckAudienceIsntSleeping implements HealthCheck {

    private Random random = new Random(System.nanoTime());

    public HealthCheckResponse call() {

        return new HealthCheckResponse() {
            @Override
            public String getName() {
                return "Audience";
            }

            @Override
            public State getState() {
                if (random.nextBoolean()) {
                    return State.UP;

                } else {
                    return State.DOWN;
                }
            }

            @Override
            public Optional<Map<String, Object>> getData() {
                return Optional.empty();
            }
        };

    }
}
