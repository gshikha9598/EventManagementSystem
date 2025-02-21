package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class EventDto {
    private String eventName;
    private String organizer;
    private LocalDateTime dateAndTime;

}
