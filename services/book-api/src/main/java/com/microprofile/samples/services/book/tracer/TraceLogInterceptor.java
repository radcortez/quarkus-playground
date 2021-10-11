package com.microprofile.samples.services.book.tracer;

import io.opentelemetry.api.trace.Tracer;
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
    @Inject
    Tracer tracer;

    @AroundInvoke
    public Object traceLog(final InvocationContext ctx) throws Exception {
        tracer.spanBuilder("book-api-user").setAttribute("user", jsonWebToken.getName()).startSpan().end();
        return ctx.proceed();
    }
}
