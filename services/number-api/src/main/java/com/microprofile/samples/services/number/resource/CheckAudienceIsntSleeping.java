package com.microprofile.samples.services.number.resource;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Health
@ApplicationScoped
public class CheckAudienceIsntSleeping implements HealthCheck {

    private Random random = new Random(System.nanoTime());

    public HealthCheckResponse call() {
        return new HealthCheckResponse() {

            final State state = random.nextBoolean() ? State.UP : State.DOWN;
            final Date dateTime = new Date();

            @Override
            public String getName() {
                return "Audience";
            }

            @Override
            public State getState() {
                return state;
            }

            @Override
            public Optional<Map<String, Object>> getData() {
                // let's say, if everyone is awake, we don't need extra data
                if (State.UP.equals(state)) {
                    return Optional.empty();
                }

                // else we send back some more information to diagnose
                final Map<String, Object> data = new HashMap<>();
                data.put("current-time", dateTime);
                data.put("country", "USA");
                return Optional.of(data);
            }
        };

    }
}
