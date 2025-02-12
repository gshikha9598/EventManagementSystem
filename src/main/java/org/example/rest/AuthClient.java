package org.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.dto.TokenResponse;
import org.jboss.resteasy.reactive.RestForm;


@Path(value="/realms/event/protocol/openid-connect")
@RegisterRestClient(configKey = "keycloakUrl")
public interface AuthClient {

        @Path("/token")
        @POST
        public TokenResponse login(
                @FormParam("grant_type") String grantType,
                @FormParam("client_id") String clientId,
                @FormParam("client_secret") String clientSecret,
                @FormParam("username")  String username,
                @FormParam("password") String password,
                @FormParam("scope") String scope
        );

        @Path("/logout")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public void logout(
                @RestForm("client_id") String client_id,
                @RestForm("refresh_token") String refresh_token,
                @RestForm("client_secret") String client_secret
        );


        @Path("/token")
        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public TokenResponse refreshToken(@RestForm("client_id") String client_id, @RestForm("refresh_token") String refresh_token,@RestForm String grant_type);

}

