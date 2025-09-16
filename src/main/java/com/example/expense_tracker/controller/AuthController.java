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
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // register always USER by default
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // always prefix with ROLE_
        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully with USER role");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole(),
                    user.isCanDelete(),
                    user.isCanEdit()
            );
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "canDelete", String.valueOf(user.isCanDelete()),
                    "canEdit", String.valueOf(user.isCanEdit())
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/users/{id}/permissions")
    public ResponseEntity<Map<String, String>> updatePermissions(@PathVariable Long id,
                                                                 @RequestBody Map<String, Boolean> body) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setCanDelete(body.getOrDefault("canDelete", user.isCanDelete()));
        user.setCanEdit(body.getOrDefault("canEdit", user.isCanEdit()));

        userRepo.save(user);

        return ResponseEntity.ok(Map.of("message", "Permissions updated")); // ✅ JSON return karega
    }


    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newRole = body.get("role");
        if (!newRole.equals("ROLE_USER") && !newRole.equals("ROLE_EDITOR") && !newRole.equals("ROLE_ADMIN")) {
            return ResponseEntity.badRequest().body("Invalid role");
        }
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        userRepo.save(user);
        return ResponseEntity.ok("User role updated to " + newRole);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}
