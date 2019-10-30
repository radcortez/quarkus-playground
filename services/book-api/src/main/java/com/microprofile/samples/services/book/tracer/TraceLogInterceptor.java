package com.microprofile.samples.services.book.tracer;

import io.opentracing.Tracer;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;

@Interceptor
@TraceLog
public class TraceLogInterceptor {
    @Inject
    JsonWebToken jsonWebToken;
    @Inject
    Tracer tracer;

    @AroundInvoke
    public Object traceLog(final InvocationContext ctx) throws Exception {
        final HashMap<String, Object> info = new HashMap<>();
        info.put("user", jsonWebToken.getName());

        tracer.activeSpan().log(info);
        return ctx.proceed();
    }
}
