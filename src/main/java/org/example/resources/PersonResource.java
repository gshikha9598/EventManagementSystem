package org.example.resources;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.example.dto.EventDto;
import org.example.dto.PersonDto;
import org.example.models.Event;
import org.example.models.Person;
import org.example.services.EventService;
import org.example.services.PersonService;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class PersonResource {

    @Inject
    PersonService personService;

    @Inject
    EventService eventService;

    @Authenticated
    @Query("getAllPersons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @Authenticated
    @Query
    public Person getPersonById(long personId) {
        Person person = personService.getPersonById(personId);
        if (person == null) {
            throw new RuntimeException("Person not found with id: " + personId);
        }
        return person;
    }

}
