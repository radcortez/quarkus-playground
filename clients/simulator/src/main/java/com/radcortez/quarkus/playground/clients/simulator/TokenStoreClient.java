package com.radcortez.quarkus.playground.clients.simulator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RegisterRestClient
public interface TokenStoreClient {
    @POST
    @Path("/oauth/token")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    Token authenticate(
        @HeaderParam("Authorization") String authorization,
        @FormParam("grant_type") String grantType,
        @FormParam("scope") String scope);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Token {
        @JsonbProperty("access_token")
        private String accessToken;
    }
}
