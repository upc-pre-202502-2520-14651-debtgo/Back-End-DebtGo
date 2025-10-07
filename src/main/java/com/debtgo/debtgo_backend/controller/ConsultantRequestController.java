package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.ConsultantRequest;
import com.debtgo.debtgo_backend.repository.ConsultantRequestRepository;
import com.debtgo.debtgo_backend.service.ConsultantAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultantRequestController {

    private final ConsultantAppService service;
    private final ConsultantRequestRepository requestRepository;

    @GetMapping("/by-consultant/{id}")
    public List<ConsultantRequest> listCases(@PathVariable Long id) {
        return service.cases(id);
    }

    @PatchMapping("/{id}/status")
    public void patchStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.updateCaseStatus(id, body.getOrDefault("status", "PENDING"));
    }

    @PostMapping
    public ResponseEntity<ConsultantRequest> createRequest(@RequestBody ConsultantRequest request) {
        // Carga el consultor completo desde la BD usando su ID
        Consultant consultant = service.findConsultantById(request.getConsultant().getId());
        request.setConsultant(consultant);

        // Guarda la solicitud con el consultor completo
        ConsultantRequest saved = requestRepository.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}