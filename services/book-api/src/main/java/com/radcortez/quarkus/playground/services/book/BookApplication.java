package com.radcortez.quarkus.playground.services.book;

import com.radcortez.quarkus.playground.services.book.model.BookCreate;
import com.radcortez.quarkus.playground.services.book.model.BookRead;
import com.radcortez.quarkus.playground.services.book.model.BookUpdate;
import com.radcortez.quarkus.playground.services.book.resource.ErrorPayload;
import org.eclipse.microprofile.auth.LoginConfig;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.OAuthScope;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.PATH;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.NUMBER;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT")
@OpenAPIDefinition(
    info = @Info(
        title = "Book API",
        version = "1.0"
    ),
    security = @SecurityRequirement(name = "oauth2", scopes = {"admin"}),
    components = @Components(
        securitySchemes = {
            @SecurityScheme(
                securitySchemeName = "oauth2",
                type = SecuritySchemeType.OAUTH2,
                in = SecuritySchemeIn.HEADER,
                flows = @OAuthFlows(
                    clientCredentials = @OAuthFlow(
                        tokenUrl = "/oauth/token",
                        scopes = {
                            @OAuthScope(name = "admin", description = "Administrator")
                        }
                    )
                )
            )
        },
        parameters = {
            @Parameter(
                name = "id",
                description = "Id of the Book to perform the operation",
                required = true,
                example = "1",
                in = PATH,
                schema = @Schema(type = NUMBER)
            )
        },
        requestBodies = {
            @RequestBody(
                name = "bookCreate",
                description = "The Book to create",
                required = true,
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "bookCreate"))
            ),
            @RequestBody(
                name = "bookUpdate",
                description = "The Book to update",
                required = true,
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "bookUpdate"))
            )
        },
        responses = {
            @APIResponse(
                name = "unauthorized",
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "error"))
            ),
            @APIResponse(
                name = "forbidden",
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "error"))
            ),
            @APIResponse(
                name = "notFound",
                responseCode = "404",
                description = "Object Not found",
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "error"))
            ),
            @APIResponse(
                name = "internalError",
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(ref = "error"))
            )
        },
        schemas = {
            @Schema(name = "error", implementation = ErrorPayload.class),
            @Schema(name = "book", implementation = BookRead.class),
            @Schema(name = "bookCreate", implementation = BookCreate.class),
            @Schema(name = "bookUpdate", implementation = BookUpdate.class),
        }
    )
)

public class BookApplication extends Application {
    // let the server discover the endpoints
}
