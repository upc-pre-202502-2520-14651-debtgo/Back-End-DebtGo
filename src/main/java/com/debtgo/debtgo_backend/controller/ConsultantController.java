package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.dto.ConsultantDto;
import com.debtgo.debtgo_backend.dto.ConsultantSummaryDto;
import com.debtgo.debtgo_backend.repository.ConsultantRepository;
import com.debtgo.debtgo_backend.service.ConsultantAppService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/v1/consultants")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultantController {

    private final ConsultantAppService service;
    private final ConsultantRepository consultantRepo;

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

    @GetMapping("/metrics/{id}")
    public Map<String, Object> metrics(@PathVariable Long id) {
        return service.metrics(id);
    }

    @GetMapping
    public List<ConsultantDto> listAll() {
        return consultantRepo.findAll()
                .stream()
                .map(c -> ConsultantDto.builder()
                        .id(c.getId())
                        .fullName(c.getFullName())
                        .specialty(c.getSpecialty())
                        .experience(c.getExperience())
                        .description(c.getDescription())
                        .profileImage(c.getProfileImage())
                        .rating(c.getRating())
                        .hourlyRate(c.getHourlyRate())
                        .build())
                .toList();
    }

    @PostMapping
    public ResponseEntity<Consultant> createConsultant(@RequestBody Consultant consultant) {
        Consultant saved = service.createConsultant(consultant);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}