package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.dto.LoginDto;
import org.example.dto.TokenResponse;
import org.example.models.Person;
import org.example.repository.PersonRepository;
import org.example.rest.AuthClient;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import java.util.Collections;

@ApplicationScoped
@Slf4j
public class AuthService {

    @Inject
    PersonRepository personRepository;

    @RestClient
    AuthClient authClient;

    Keycloak keycloak;


    @ConfigProperty(name = "keycloak_realm")
    private String keycloakRealm;

    @PostConstruct  //method call automatically when object use first time use like- @Observes StartupEvent
    public void initKeycloak() { //initialize keycloak object
        keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8081")
                .realm(keycloakRealm)
                .clientId("event")
                .clientSecret("sHlqht10GxpyNaMzmiJlSSlpfWp632Ca")
                .grantType("client_credentials")
                .build();
    }

    @PreDestroy
    public void closeKeycloak() {
        keycloak.close();
    }


    public String createUser(Person person) throws JsonProcessingException { //user register

        UserRepresentation keyclockUser = new UserRepresentation();

        keyclockUser.setEmail(person.getEmailId());
        keyclockUser.setFirstName(person.getFirstName());
        keyclockUser.setLastName(person.getLastName());
        keyclockUser.setUsername(person.getEmailId());
        keyclockUser.setEnabled(true);
        keyclockUser.setEmailVerified(true);
        keyclockUser.setRealmRoles(Collections.singletonList("user"));


        if (person.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(person.getPassword());
            credentialRepresentation.setTemporary(false);

            keyclockUser.setCredentials(Collections.singletonList(credentialRepresentation));

        }

        Response response = keycloak.realm(keycloakRealm).users().create(keyclockUser);
        int statusCode = response.getStatus();
        String responseBody = response.readEntity(String.class);  // Extract the response body

        log.info("Response Status: {}", statusCode);
        log.info("Response Body: {}", responseBody);

        if (statusCode == 201) {
            personRepository.createUser(person);
            return "User created successfully";
        } else {
            return "User creation failed: " + responseBody;
        }

    }

    public TokenResponse login(LoginDto loginDto) {
        try {
            TokenResponse tokenResponse = authClient.login(  //send these details for login from keycloak
                    "password",
                    "event",
                    "sHlqht10GxpyNaMzmiJlSSlpfWp632Ca",
                    loginDto.getUsername(),
                    loginDto.getPassword(),
                    "openid"
            );

            return tokenResponse;
        } catch (Exception e) {
            log.error("error while login ", e);
            throw new RuntimeException(e);
        }
    }

    public String logout(String refresh_token) {
        authClient.logout("event", refresh_token,"sHlqht10GxpyNaMzmiJlSSlpfWp632Ca");
        return "logged out";
    }

    public TokenResponse refreshToken(String refresh_token) {
        TokenResponse tokenResponse = authClient.refreshToken("event", refresh_token, "refresh_token");
        return tokenResponse;
    }

}
