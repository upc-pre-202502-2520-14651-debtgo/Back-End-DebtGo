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
@CrossOrigin(origins = "*") // Mejora para deploy
public class ConsultantController {

    private final ConsultantAppService service;

    @GetMapping("/{id}")
    public ConsultantDto get(@PathVariable Long id) {
        return service.getConsultant(id);
    }

    @PutMapping("/{id}")
    public ConsultantDto update(@PathVariable Long id, @RequestBody ConsultantDto dto) {
        return service.updateConsultant(id, dto);
    }

    @GetMapping("/{id}/summary")
    public ConsultantSummaryDto summary(@PathVariable Long id) {
        return service.getSummary(id);
    }

    @GetMapping("/{id}/services")
    public List<ConsultantServiceDto> listServices(@PathVariable Long id) {
        return service.getServices(id);
    }

    @PostMapping("/services")
    public ConsultantServiceDto createService(@RequestBody ConsultantServiceDto dto) {
        return service.createService(dto);
    }

    @PutMapping("/services/{id}")
    public ConsultantServiceDto updateService(
            @PathVariable Long id,
            @RequestBody ConsultantServiceDto dto) {

        return service.updateService(id, dto);
    }

    @DeleteMapping("/services/{id}")
    public void deleteService(@PathVariable Long id) {
        service.deleteService(id);
    }

    @GetMapping
    public List<ConsultantDto> listarConsultores() {
        return service.findAll();
    }
}