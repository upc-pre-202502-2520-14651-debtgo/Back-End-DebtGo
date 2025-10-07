package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.debt.Debt;
import com.debtgo.debtgo_backend.dto.debt.DebtSummaryDto;
import com.debtgo.debtgo_backend.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/debts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DebtController {

    private final DebtService debtService;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return debtService.getSummary();
    }

    @GetMapping("/list")
    public List<DebtSummaryDto> getAllDebts() {
        return debtService.getAllDebts();
    }

    // ✅ Nuevo endpoint para crear deuda
    @PostMapping("/create")
    public Debt createDebt(@RequestBody DebtSummaryDto dto) {
        return debtService.createDebt(dto);
    }
}