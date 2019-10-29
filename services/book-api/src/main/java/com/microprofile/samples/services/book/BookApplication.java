package com.microprofile.samples.services.book;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class BookApplication extends Application {
    // let the server discover the endpoints
}
