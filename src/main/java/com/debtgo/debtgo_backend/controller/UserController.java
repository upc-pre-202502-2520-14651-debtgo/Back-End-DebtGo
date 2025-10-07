package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.dto.LoginRequest;
import com.debtgo.debtgo_backend.dto.LoginResponse;
import com.debtgo.debtgo_backend.dto.RegisterRequest;
import com.debtgo.debtgo_backend.dto.RegisterResponse;
import com.debtgo.debtgo_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setRole(request.getRole());

        User savedUser = userService.registerUser(newUser);

        return ResponseEntity.ok(
                new RegisterResponse(true, "User registered successfully", savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        return userService.loginUser(email, password)
                .map(u -> ResponseEntity.ok(new LoginResponse(true, "Login successful", u)))
                .orElse(ResponseEntity.status(401).body(new LoginResponse(false, "Invalid credentials", null)));
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}