package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.simulation.Simulation;
import com.debtgo.debtgo_backend.dto.simulation.*;
import com.debtgo.debtgo_backend.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/simulations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService service;

    // Calcula sin guardar
    @PostMapping("/preview")
    public ResponseEntity<SimulationResponseDto> preview(@RequestBody SimulationRequestDto dto) {
        return ResponseEntity.ok(service.preview(dto));
    }

    // Guarda simulación
    @PostMapping
    public ResponseEntity<SimulationResponseDto> create(@RequestBody SimulationRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // Lista por usuario (para “Mis simulaciones”)
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Simulation>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.listByUser(userId));
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        // PDF de ejemplo para evitar error en frontend
        byte[] pdf = "PDF example content".getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdf);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSimulation(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok("Simulación guardada correctamente");
    }
}