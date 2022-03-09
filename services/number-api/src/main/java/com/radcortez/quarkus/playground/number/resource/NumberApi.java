package com.radcortez.quarkus.playground.number.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/numbers")
@Produces(MediaType.TEXT_PLAIN)
@Tag(name = "Number Resource")
public interface NumberApi {
    @GET
    @Path("/generate")
    @Operation(description = "Generate an ISBN for a book.")
    @APIResponse(
        responseCode = "200",
        description = "Successful, returning the value",
        content = @Content(
            mediaType = MediaType.TEXT_PLAIN,
            schema = @Schema(
                implementation = String.class
            )
        ))
    Response generate();
}
