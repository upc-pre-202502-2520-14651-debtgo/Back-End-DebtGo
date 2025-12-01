package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.dto.LoginRequest;
import com.debtgo.debtgo_backend.dto.RegisterRequest;
import com.debtgo.debtgo_backend.repository.UserRepository;
import com.debtgo.debtgo_backend.repository.ConsultantRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;
    private final ConsultantRepository consultantRepository;

    // ----------------------
    // REGISTRO
    // ----------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());

        User saved = userRepository.save(user);

        if ("CONSULTANT".equalsIgnoreCase(saved.getRole())) {

            Consultant c = Consultant.builder()
                    .fullName(req.getName())
                    .specialty("Sin definir")
                    .experience("0 años")
                    .description("Perfil en creación")
                    .hourlyRate(0.0)
                    .rating(0.0)
                    .profileImage("default.jpg")
                    .build();

            consultantRepository.save(c);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id", saved.getId());
        body.put("name", saved.getName());
        body.put("email", saved.getEmail());
        body.put("role", saved.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // ----------------------
    // LOGIN
    // ----------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        Long consultantId = null;
        if ("CONSULTANT".equals(user.getRole())) {
            consultantId = consultantRepository.findById(user.getId())
                    .map(Consultant::getId)
                    .orElse(null);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id", user.getId());
        body.put("email", user.getEmail());
        body.put("role", user.getRole());
        body.put("consultantId", consultantId);

        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}