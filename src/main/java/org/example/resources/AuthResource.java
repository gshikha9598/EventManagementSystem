package org.example.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.example.dto.LoginDto;
import org.example.dto.PersonDto;
import org.example.dto.TokenResponse;
import org.example.models.Person;
import org.example.services.AuthService;

@ApplicationScoped
@GraphQLApi
@Slf4j
public class AuthResource {
    @Inject
    private AuthService authService;


    @Mutation
    public String createUser(PersonDto personDto) throws JsonProcessingException {
        Person person = new Person();
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmailId(personDto.getEmailId());
        person.setPassword(personDto.getPassword());

         authService.createUser(person);
          return "user created";
    }

    @Mutation
    public TokenResponse login(LoginDto loginDto){
        return authService.login(loginDto);
    }

    @Mutation
    public String logout(String refresh_token) {
        authService.logout(refresh_token);
        return "logged out";
    }

    @Mutation
    public TokenResponse refreshToken(String refresh_token){
        return authService.refreshToken(refresh_token);
    }


}
