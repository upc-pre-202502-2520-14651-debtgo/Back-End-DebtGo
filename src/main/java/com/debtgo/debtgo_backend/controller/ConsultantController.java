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

    @GetMapping
    public List<ConsultantDto> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ConsultantDto get(@PathVariable Long id) {
        return service.getConsultant(id);
    }

    @GetMapping("/{id}/summary")
    public ConsultantSummaryDto summary(@PathVariable Long id) {
        return service.getSummary(id);
    }

    @GetMapping("/{id}/services")
    public List<ConsultantServiceDto> services(@PathVariable Long id) {
        return service.byConsultant(id);
    }

    @GetMapping("/by-consultant/{consultantId}")
    public List<ConsultantServiceDto> getServicesByConsultant(
            @PathVariable Long consultantId) {
        return service.getServices(consultantId);
    }
}