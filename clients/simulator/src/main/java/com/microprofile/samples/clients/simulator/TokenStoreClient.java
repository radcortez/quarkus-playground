package com.microprofile.samples.clients.simulator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.bind.annotation.JsonbProperty;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
