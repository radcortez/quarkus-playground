package com.radcortez.quarkus.playground.services.book.tracer;

import io.opentelemetry.api.trace.Span;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
@TraceLog
public class TraceLogInterceptor {
    @Inject
    JsonWebToken jsonWebToken;

    @AroundInvoke
    public Object traceLog(final InvocationContext ctx) throws Exception {
        Span.current().setAttribute("user", jsonWebToken.getName());
        return ctx.proceed();
    }
}
