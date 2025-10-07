package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.dto.AdvisoryDto;
import com.debtgo.debtgo_backend.dto.AdvisoryResponseDto;
import com.debtgo.debtgo_backend.service.AdvisoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisories")
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    public AdvisoryController(AdvisoryService advisoryService) {
        this.advisoryService = advisoryService;
    }

    @PostMapping
    public ResponseEntity<AdvisoryResponseDto> createAdvisory(@RequestParam Long entrepreneurId,
            @RequestBody AdvisoryDto dto) {
        Advisory saved = advisoryService.createAdvisory(entrepreneurId, dto.toEntity());
        return ResponseEntity.ok(advisoryService.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<AdvisoryResponseDto>> getAllAdvisories() {
        return ResponseEntity.ok(
                advisoryService.getAll()
                        .stream()
                        .map(advisoryService::toDto)
                        .toList());
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignConsultant(@PathVariable Long id,
            @RequestParam Long consultantId) {
        return advisoryService.assignConsultant(id, consultantId)
                .<ResponseEntity<?>>map(advisory -> ResponseEntity.ok(advisoryService.toDto(advisory)))
                .orElseGet(() -> ResponseEntity.badRequest().body("Assignment failed"));
    }
}