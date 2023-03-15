package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.NumberConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class NumberResource implements NumberApi {
    @Inject
    NumberConfig numberConfig;

    @Override
    public Response generate() {
        return Response.ok(generateNumber()).build();
    }

    String generateNumber() {
        return numberConfig.generation().prefix() + "-" + (int) Math.floor((Math.random() * 9999999)) + 1;
    }
}
