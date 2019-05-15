package com.microprofile.samples.services.book.service;

import com.microprofile.samples.services.book.resource.BookResource;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import javax.enterprise.context.RequestScoped;
import java.util.logging.Logger;

@RequestScoped
public class NumberFallbackHandler implements FallbackHandler<String> {
    private static final Logger LOGGER = Logger.getLogger(NumberFallbackHandler.class.getName());

    @Override
    public String handle(final ExecutionContext context) {
        LOGGER.info("Number Fallback.");
        return "FALLBACK_NUMBER";
    }
}
