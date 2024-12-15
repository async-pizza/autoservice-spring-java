package org.autoservice.controller;

import org.autoservice.dto.LoginRequest;
import org.autoservice.dto.RegistrationRequest;
import org.autoservice.model.User;
import org.autoservice.security.SessionManager;
import org.autoservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        User user = new User(null, registrationRequest.name(), registrationRequest.email(), registrationRequest.password(), registrationRequest.role());
        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User existingUser = userService.findByEmail(loginRequest.email());
        if (existingUser == null || !passwordEncoder.matches(loginRequest.password(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Generate a session ID or token
        String sessionId = generateSessionId();
        // Store the session ID in a cookie
        Cookie sessionCookie = new Cookie("sessionId", sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(86400); // 1 day
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        // Create session
        SessionManager.createSession(sessionId, existingUser);

        return ResponseEntity.ok("Login successful!");
    }

    private String generateSessionId() {
        // Generate a random session ID
        return java.util.UUID.randomUUID().toString();
    }
}