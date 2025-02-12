package org.example.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.Query;
import org.example.models.Event;
import org.example.models.Person;
import org.example.repository.PersonRepository;

import java.util.List;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonRepository personRepository;

    @Query
    public List<Person> getAllPersons() {
        return personRepository.listAll();
    }

    public Person getPersonById(long id) {
        return personRepository.findById(id);
    }

}
