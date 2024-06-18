package com.radcortez.quarkus.playground.clients.simulator;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.context.ManagedExecutor;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class Initializer {
    @Inject
    ManagedExecutor managedExecutor;
    @Inject
    Instance<ScenarioInvoker> scenarioInvokers;

    @PostConstruct
    public void init() {
        scenarioInvokers.forEach(managedExecutor::execute);
    }
}
