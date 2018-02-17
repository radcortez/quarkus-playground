/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.radcortez.microprofile.samples.clients.simulator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class ScenarioInvoker implements Runnable {
    private boolean execute;
    private WeightedRandomResult<Supplier<Response>> endpointsToExecute;

    protected abstract List<Supplier<Response>> getEndpoints();

    @PostConstruct
    private void init() {
        getEndpoints();
        this.execute = true;
        this.endpointsToExecute = new WeightedRandomResult<>(getEndpoints());
    }

    @Override
    public void run() {
        while (execute) {
            try {
                final Response response = endpointsToExecute.get().get();
                System.out.println(response.getStatus());
                sleep();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000); // TODO - Externalize
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
