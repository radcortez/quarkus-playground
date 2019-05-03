package com.microprofile.samples.services.book.resource;

import javax.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
public class ApplicationConfig extends Application {
    // let the server discover the endpoints
}