package com.example.expense_tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity // Tells Spring this is a DB table.
@Data  // Adds getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id //Marks id as primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String role = "USER";
}
