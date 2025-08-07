package com.example.expense_tracker.controller;

import com.example.expense_tracker.dto.AuthRequest;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.UserRepository;
import com.example.expense_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController //Handles HTTP requests.
@RequestMapping("/api/auth") // All endpoints start with /api/auth.
public class AuthController {

    @Autowired  // Injects UserRepository.
    private UserRepository userRepo;

    @Autowired  //Injects PasswordEncoder.
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register") //Handles POST request to /api/auth/register.
    public ResponseEntity<String> register(@RequestBody User user) { //Gets user data from request body.
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }// Checks if username exists → if yes, returns error.
        //Else:
        user.setPassword(passwordEncoder.encode(user.getPassword())); //Encrypts password.
        userRepo.save(user); //Saves user in DB.
        return ResponseEntity.ok("User registered successfully"); //Sends success message.
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
