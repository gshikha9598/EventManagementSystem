package org.example.resources;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.example.dto.EventDto;
import org.example.models.Event;
import org.example.services.EventService;
import org.example.services.KafkaService;
import org.keycloak.representations.JsonWebToken;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class EventResource {

    @Inject
    EventService eventService;

    @Inject
    KafkaService kafkaService;

    @Authenticated
    @Query("getAllEvent")
    public List<Event> getAllEvent(){
        return eventService.getAllEvent();
    }


    @Authenticated
    @Query
    public Event getEventById(long eventId){
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            throw new RuntimeException("Event not found with id: " + eventId);
        }
        return event;
    }

    @Mutation
    @RolesAllowed("admin")
    public String createEvent(EventDto eventdto) {
        Event event = new Event();
        event.setEventName(eventdto.getEventName());
        event.setOrganizer(eventdto.getOrganizer());
        event.setDateAndTime(eventdto.getDateAndTime());

        eventService.createEvent(event);

        return "Event created and published to Kafka";
    }


    @Mutation
    @RolesAllowed("admin")
    public String updatedEvent(Event event){
        eventService.updatedEvent(event);
        return "event updated";
    }

    @Mutation
    @RolesAllowed("admin")
    public String deleteEventById(long eventId){
        eventService.deleteEventById(eventId);
        return "event deleted";
    }

    //@Authenticated
    @Mutation
    public String registerUserWithEvent(long userId, long eventId){  // Register user for an event (User-related operation)
        try {
            eventService.registerUserWithEvent(userId, eventId);
            return "User registered for event successfully";
        } catch (Exception e) {
            return "Error during event registration: " + e.getMessage();
        }
    }

}
