package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.MetricsDto;
import com.debtgo.debtgo_backend.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultants/metrics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsService service;

    @GetMapping("/{id}")
    public MetricsDto getMetrics(@PathVariable Long id) {
        return service.getMetrics(id);
    }
}