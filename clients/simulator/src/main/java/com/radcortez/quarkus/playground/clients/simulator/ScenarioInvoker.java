package com.radcortez.quarkus.playground.clients.simulator;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class ScenarioInvoker implements Runnable {
    private boolean execute;
    private WeightedRandomResult<Supplier<Response>> endpointsToExecute;
    private Logger logger;

    protected abstract List<Supplier<Response>> getEndpoints();

    @PostConstruct
    private void init() {
        getEndpoints();
        this.execute = true;
        this.endpointsToExecute = new WeightedRandomResult<>(getEndpoints());
        this.logger = Logger.getLogger(getClass().getName());
    }

    @Override
    public void run() {
        while (execute) {
            try {
                final Response response = endpointsToExecute.get().get();
                logger.info("Simulator got a new HTTP response: " + response.getStatus());
                sleep();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destroy() {
        this.execute = false;
    }

    private static class WeightedRandomResult<T> implements Supplier<T> {
        private final Random random = new Random();
        private final List<T> results = new ArrayList<>();

        WeightedRandomResult(final Collection<T> results) {
            this.results.addAll(results);
        }

        public T get() {
            return this.results.get(random.nextInt(this.results.size()));
        }
    }
}
