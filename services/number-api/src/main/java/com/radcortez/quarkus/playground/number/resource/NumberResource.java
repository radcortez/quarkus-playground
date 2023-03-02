package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.GenerationPrefix;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class NumberResource implements NumberApi {
    @Inject
    @ConfigProperty(name = "generation.prefix", defaultValue = "UN")
    GenerationPrefix prefix;

    @Override
    public Response generate() {
        return Response.ok(generateNumber()).build();
    }

    private String generateNumber() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (final InterruptedException e) {
            //
        }

        return prefix.toString() + "-" + (int) Math.floor((Math.random() * 9999999)) + 1;
    }
}
