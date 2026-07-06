package com.radcortez.quarkus.playground.services.book.resource;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;
import java.util.Optional;

public class NumberApiResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {
    private Optional<String> containerNetworkId;
    private GenericContainer<?> container;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @Override
    public Map<String, String> start() {
        container = new GenericContainer<>("docker-registry:5000/radcortez/number-api")
                .withExposedPorts(8090);
        containerNetworkId.ifPresent(container::withNetworkMode);
        container.start();

        String host = containerNetworkId
                .map(c -> container.getCurrentContainerInfo().getConfig().getHostName())
                .orElse(container.getHost());
        Integer mappedPort = container.getMappedPort(8090);

        return Map.of("quarkus.rest-client.number-api.url", "http://" + host + ":" + mappedPort);
    }

    @Override
    public void stop() {
        container.stop();
    }
}
