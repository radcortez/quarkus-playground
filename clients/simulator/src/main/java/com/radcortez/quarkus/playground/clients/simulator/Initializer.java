package com.radcortez.quarkus.playground.clients.simulator;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.context.ManagedExecutor;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@QuarkusMain
public class Initializer implements QuarkusApplication {
    @Inject
    ManagedExecutor managedExecutor;
    @Inject
    Instance<ScenarioInvoker> scenarioInvokers;

    @Override
    public int run(String... args) {
        System.out.println("Initializer.run");
        scenarioInvokers.forEach(managedExecutor::execute);
        return 0;
    }
}
