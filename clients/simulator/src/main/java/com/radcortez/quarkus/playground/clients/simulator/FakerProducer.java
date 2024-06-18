package com.radcortez.quarkus.playground.clients.simulator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import net.datafaker.Faker;

@ApplicationScoped
public class FakerProducer {
    @Produces
    public Faker createFaker(final InjectionPoint injectionPoint) {
        return new Faker();
    }
}
