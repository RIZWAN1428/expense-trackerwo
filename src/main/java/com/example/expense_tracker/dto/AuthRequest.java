package com.example.expense_tracker.dto;
//DTO (Data Transfer Object) for login.
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
