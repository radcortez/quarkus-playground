package com.microprofile.samples.services.number;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
    info = @Info(
        title = "Number API",
        version = "1.0"
    ),
    tags = @Tag(name = "Number Resource", description = "Generate a random number with a prefix."),
    servers = {
        @Server(url = "{scheme}://{host}:{port}/", description = "App Endpoint Server",
                variables = {
                    @ServerVariable(name = "scheme",
                                    enumeration = {
                                        "http",
                                        "https"
                                    },
                                    defaultValue = "http"),
                    @ServerVariable(name = "host", enumeration = {"localhost"}, defaultValue = "localhost"),
                    @ServerVariable(name = "port", enumeration = {"8090"}, defaultValue = "8090")
                })
    }
)
public class NumberApplication extends Application {

}
