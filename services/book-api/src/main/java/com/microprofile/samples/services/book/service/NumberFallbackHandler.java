package com.microprofile.samples.services.book.service;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class NumberFallbackHandler implements FallbackHandler<String> {
    @Override
    public String handle(final ExecutionContext context) {
        return "FALLBACK_NUMBER";
    }
}
