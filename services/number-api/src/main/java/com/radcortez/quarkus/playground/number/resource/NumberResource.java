package com.radcortez.quarkus.playground.number.resource;

import com.radcortez.quarkus.playground.number.config.NumberConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class NumberResource implements NumberApi {
    @Inject
    NumberConfig numberConfig;

    public enum Profile {
        admin,
        user
    }

    @ConfigProperty(name = "agogos.cli.profile", defaultValue = "user")
    Profile profile;

    @Override
    public Response generate() {
        System.out.println("profile = " + profile);
        return Response.ok(generateNumber()).build();
    }

    String generateNumber() {
        return numberConfig.generation().prefix() + "-" + (int) Math.floor((Math.random() * 9999999)) + 1;
    }
}
