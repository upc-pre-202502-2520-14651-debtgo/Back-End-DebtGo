package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.CaseRequestDto;
import com.debtgo.debtgo_backend.service.CaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultants/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CaseRequestController {

    private final CaseRequestService service;

    @GetMapping("/by-consultant/{id}")
    public List<CaseRequestDto> byConsultant(@PathVariable Long id) {
        return service.byConsultant(id);
    }
}