package org.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String emailId;
    @Transient //not save to db
    private String password;

    @ManyToMany(mappedBy = "registeredUsers")
    private List<Event> eventList;

    //private String role;  // 'admin' or 'user'

}
