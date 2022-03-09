package com.radcortez.quarkus.playground.services.book.auth;

import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
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
