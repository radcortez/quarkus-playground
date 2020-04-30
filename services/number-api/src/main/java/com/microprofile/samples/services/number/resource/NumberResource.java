package com.microprofile.samples.services.number.resource;

import com.microprofile.samples.services.number.config.GenerationPrefix;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class NumberResource implements NumberApi {
    @Inject
    @ConfigProperty(name = "generation.prefix", defaultValue = "UN")
    private GenerationPrefix prefix;

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
