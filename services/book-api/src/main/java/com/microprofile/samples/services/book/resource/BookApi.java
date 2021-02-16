package com.microprofile.samples.services.book.resource;

import com.microprofile.samples.services.book.model.BookCreate;
import com.microprofile.samples.services.book.model.BookUpdate;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.NUMBER;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.STRING;

@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Tag(name = "Book API")
@SecurityRequirement(name = "oauth2", scopes = {"admin"})
public interface BookApi {
    @GET
    @Path("/{id}")
    @Operation(
        operationId = "GetBook",
        summary = "Find a Book by Id"
    )
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "book")))
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbidden")
    @APIResponse(responseCode = "404", ref = "notFound")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response get(
        @PathParam("id")
        @Parameter(ref = "id")
        Long id);

    @GET
    @Operation(
        operationId = "GetBooks",
        summary = "Find all Books"
    )
    @APIResponse(responseCode = "200",
                 content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(type = ARRAY, ref = "book")))
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbidden")
    @APIResponse(responseCode = "404", ref = "notFound")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response get();

    @POST
    @RolesAllowed("admin")
    @Operation(
        operationId = "CreateBook",
        summary = "Create a new Book"
    )
    @RequestBody(ref = "bookCreate")
    @APIResponse(
        responseCode = "201",
        content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "book")),
        headers = @Header(
            name = "Location",
            description = "Information about the location of a newly created resource")
    )
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbidden")
    @APIResponse(responseCode = "404", ref = "notFound")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response create(BookCreate bookCreate);

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(
        operationId = "UpdateBook",
        summary = "Update an existent Book"
    )
    @RequestBody(ref = "bookUpdate")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "book")))
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbidden")
    @APIResponse(responseCode = "404", ref = "notFound")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response update(
        @PathParam("id")
        @Parameter(ref = "id")
            Long id,
            BookUpdate bookUpdate);

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(
        operationId = "DeleteBook",
        summary = "Delete an existent Book"
    )
    @APIResponse(responseCode = "204", description = "Book successfully deleted")
    @APIResponse(responseCode = "401", ref = "unauthorized")
    @APIResponse(responseCode = "403", ref = "forbidden")
    @APIResponse(responseCode = "404", ref = "notFound")
    @APIResponse(responseCode = "500", ref = "internalError")
    Response delete(
        @PathParam("id")
        @Parameter(ref = "id")
            Long id);
}
