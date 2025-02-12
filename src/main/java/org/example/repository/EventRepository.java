package org.example.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.models.Event;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {
}
