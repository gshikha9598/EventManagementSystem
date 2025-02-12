package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.example.models.Event;
import org.example.models.Person;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person>  {

    @Transactional
    public void createUser(Person person){
        persist(person);
    }
}
