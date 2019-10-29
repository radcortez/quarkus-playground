package com.microprofile.samples.services.book.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

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

import static com.microprofile.samples.services.book.auth.TokenUtils.generateTokenString;

@Path("/oauth/token")
public class TokenEndpoint {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response token(@HeaderParam("Authorization") final String authorization, final Form form) {
        final BasicAuthCredentials credentials = BasicAuthCredentials.createCredentialsFromHeader(authorization);
        final String accessToken = generateTokenString(credentials.getUsername(), getGroups(form));
        return Response.ok(Token.token(accessToken)).build();
    }

    private Set<String> getGroups(final Form form) {
        final MultivaluedMap<String, String> parameters = form.asMap();
        final String scope = parameters.getFirst("scope");
        if (scope == null) {
            return new HashSet<>();
        }
        return Stream.of(scope.split(" ")).collect(Collectors.toSet());
    }

    @AllArgsConstructor(staticName = "token")
    @Data
    public static class Token {
        private String access_token;
    }
}
