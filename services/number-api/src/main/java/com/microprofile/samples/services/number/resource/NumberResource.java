package com.microprofile.samples.services.number.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("numbers")
@Produces(MediaType.TEXT_PLAIN)
@Traced
@Tag(name = "ISBN resource", description = "Generate an ISBN number for a book.")
public class NumberResource {
    private final Logger logger = Logger.getLogger(NumberResource.class.getName());

    @Inject
    @ConfigProperty(name = "generation.prefix", defaultValue = "UN")
    private GenerationPrefix prefix;

    @Inject
    @Claim("username")
    private ClaimValue<String> username;

    @GET
    @Path("/generate")
    @Metered(description = "Metrics for ISBN random generation")
    @Timed(description = "Metrics to monitor the times of generate ISBN method.",
            unit = MetricUnits.MILLISECONDS,
            absolute = true)
    @Operation(description = "Generate an ISBN for a book.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Successful, returning the value",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    ))
    })
    @RolesAllowed("number-api")
    public Response generate() {

        // this uses the information in the JWT
        // after it gets validated, we'll get the "username" claim injected directly
        logger.info(String.format("User `%s` called Number API.", username.getValue()));

        return Response.ok(number()).build();
    }

    @GET
    @Path("/generate/guest")
    public Response generateGuest() {
        return Response.ok(number()).build();
    }

    private String number() {
        return prefix.toString() + "-" +(int) Math.floor((Math.random() * 9999999)) + 1;
    }
}
