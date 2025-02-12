package org.example.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.models.Event;
import org.example.models.Person;
import org.example.repository.EventRepository;
import org.example.repository.PersonRepository;

import java.util.List;

@ApplicationScoped
public class EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    PersonRepository personRepository;

    @Inject
    KafkaService kafkaService;

    @Inject
    EntityManager entityManager;

    public List<Event> getAllEvent(){

        return eventRepository.listAll();
    }

    public Event getEventById(long eventId){

        return eventRepository.findById(eventId);
    }

    @Transactional
    public void createEvent(Event event){
        eventRepository.persist(event);
        kafkaService.publishEventMessage(event);
    }

    @Transactional
    public void updatedEvent(Event event){
        entityManager.merge(event); //eventRepository.persist(event);
    }

    @Transactional
    public void deleteEventById(long eventId){
        eventRepository.deleteById(eventId);
    }

    @Transactional
    public void registerUserWithEvent(long userId, long eventId){
        Person person = personRepository.findById(userId);
        Event event = eventRepository.findById(eventId);
        event.getRegisteredUsers().add(person);
        person.getEventList().add(event);
        entityManager.merge(event);
        entityManager.merge(person);
    }
}
