package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.domain.profile.EntrepreneurProfile;
import com.debtgo.debtgo_backend.dto.LoginRequest;
import com.debtgo.debtgo_backend.dto.RegisterRequest;
import com.debtgo.debtgo_backend.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final ConsultantRepository consultantRepository;
    private final EntrepreneurProfileRepository entrepreneurRepo;

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

        // ============================
        // CREAR PERFIL SI ES CONSULTOR
        // ============================
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

        // ============================
        // CREAR PERFIL SI ES EMPRENDEDOR
        // ============================
        if ("ENTREPRENEUR".equalsIgnoreCase(saved.getRole())) {

            EntrepreneurProfile ep = new EntrepreneurProfile();
            ep.setName(req.getName());
            ep.setEmail(req.getEmail());
            ep.setPhone("Sin número");

            entrepreneurRepo.save(ep);
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
            Optional<Consultant> consultant = consultantRepository.findAll()
                    .stream()
                    .filter(c -> c.getFullName().equalsIgnoreCase(user.getName()))
                    .findFirst();

            consultantId = consultant.map(Consultant::getId).orElse(null);
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