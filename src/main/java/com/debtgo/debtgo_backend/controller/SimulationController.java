package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.simulation.Simulation;
import com.debtgo.debtgo_backend.dto.simulation.*;
import com.debtgo.debtgo_backend.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/simulations")
@CrossOrigin(origins = "http://localhost:4200")
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
}