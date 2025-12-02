package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.ConsultantDto;
import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.dto.ConsultantSummaryDto;
import com.debtgo.debtgo_backend.service.ConsultantAppService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsultantController {

    private final ConsultantAppService service;

    /**
     * Devuelve un consultor por ID (lo usa la vista Consultant Details)
     */
    @GetMapping("/{id}")
    public ConsultantDto get(@PathVariable Long id) {
        return service.getConsultant(id);
    }

    /**
     * Actualiza el perfil del consultor (vista editar perfil)
     */
    @PutMapping("/{id}")
    public ConsultantDto update(@PathVariable Long id, @RequestBody ConsultantDto dto) {
        return service.updateConsultant(id, dto);
    }

    /**
     * Resumen del consultor (dashboard)
     */
    @GetMapping("/{id}/summary")
    public ConsultantSummaryDto summary(@PathVariable Long id) {
        return service.getSummary(id);
    }

    /**
     * Servicios ofrecidos por consultor
     */
    @GetMapping("/{id}/services")
    public List<ConsultantServiceDto> listServices(@PathVariable Long id) {
        return service.getServices(id);
    }

    /**
     * Crear servicio
     */
    @PostMapping("/services")
    public ConsultantServiceDto createService(@RequestBody ConsultantServiceDto dto) {
        return service.createService(dto);
    }

    /**
     * Actualizar servicio
     */
    @PutMapping("/services/{id}")
    public ConsultantServiceDto updateService(
            @PathVariable Long id,
            @RequestBody ConsultantServiceDto dto) {
        return service.updateService(id, dto);
    }

    /**
     * Eliminar servicio
     */
    @DeleteMapping("/services/{id}")
    public void deleteService(@PathVariable Long id) {
        service.deleteService(id);
    }

    /**
     * LISTA DE CONSULTANTS
     * (lo usa Emprendedores en el frontend)
     */
    @GetMapping
    public List<ConsultantDto> listarConsultores() {
        return service.findAll();
    }

    /**
     * Lista de consultores por usuario (lo usa historial)
     */
    @GetMapping("/by-user/{userId}")
    public List<ConsultantDto> listarPorUsuario(@PathVariable Long userId) {
        return service.findAllByUser(userId);
    }
}