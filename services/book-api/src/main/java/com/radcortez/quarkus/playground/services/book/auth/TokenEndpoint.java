package com.radcortez.quarkus.playground.services.book.auth;

import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.json.Json;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.radcortez.quarkus.playground.services.book.auth.TokenUtils.generateTokenString;

@Path("/oauth/token")
public class TokenEndpoint {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(hidden = true)
    public Response token(@HeaderParam("Authorization") final String authorization, final Form form) {
        final BasicAuthCredentials credentials = BasicAuthCredentials.createCredentialsFromHeader(authorization);
        final String accessToken = generateTokenString(credentials.getUsername(), getGroups(form));
        return Response.ok(Json.createObjectBuilder().add("access_token", accessToken).build()).build();
    }

    private Set<String> getGroups(final Form form) {
        final MultivaluedMap<String, String> parameters = form.asMap();
        final String scope = parameters.getFirst("scope");
        if (scope == null) {
            return new HashSet<>();
        }
        return Stream.of(scope.split(" ")).collect(Collectors.toSet());
    }
}
