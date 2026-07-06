package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.NumberConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class NumberResource implements NumberApi {
    @Inject
    NumberConfig numberConfig;

    @Override
    public Response generate() {
        return Response.ok(generateNumber()).build();
    }

    String generateNumber() {
        long min = (long) Math.pow(10, numberConfig.generation().length() - 1);
        long max = (long) Math.pow(10, numberConfig.generation().length());
        return numberConfig.generation().prefix() + "-" + ThreadLocalRandom.current().nextLong(min, max);
    }
}
