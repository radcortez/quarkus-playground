package com.microprofile.samples.services.book.client;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

public class NumberApiFallback implements FallbackHandler<String> {
    @Override
    public String handle(final ExecutionContext executionContext) {
        return "ISBN-FALLBACK";
    }
}
