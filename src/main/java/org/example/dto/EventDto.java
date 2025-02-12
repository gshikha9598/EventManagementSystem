package org.example.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.example.models.Person;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDto {
    private String eventName;
    private String organizer;
    private LocalDateTime dateAndTime;

}
