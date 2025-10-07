package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.ConsultantDashboardDto;
import com.debtgo.debtgo_backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/consultants/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{consultantId}")
    public ResponseEntity<ConsultantDashboardDto> getDashboard(@PathVariable Long consultantId) {
        return ResponseEntity.ok(dashboardService.getConsultantDashboard(consultantId));
    }
}
