package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.debt.Debt;
import com.debtgo.debtgo_backend.dto.debt.DebtSummaryDto;
import com.debtgo.debtgo_backend.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;

    // Devuelve las métricas generales
    public Map<String, Object> getSummary() {
        return Map.of(
                "activeClients", debtRepository.countActiveClients(),
                "overdueDebts", debtRepository.countByStatus("LATE"),
                "activePlans", debtRepository.countByStatus("ACTIVE"),
                "complianceRate", debtRepository.calculateComplianceRate());
    }

    // Lista todas las deudas
    public List<DebtSummaryDto> getAllDebts() {
        return debtRepository.findAll()
                .stream()
                .map(d -> new DebtSummaryDto(
                        d.getId(),
                        d.getClientName(),
                        d.getTotalDebt(),
                        d.getMonthlyPayment(),
                        d.getDueDate(),
                        d.getStatus()))
                .collect(Collectors.toList());
    }

    // Crea una nueva deuda
    public Debt createDebt(DebtSummaryDto dto) {
        Debt debt = Debt.builder()
                .clientName(dto.getClientName())
                .totalDebt(dto.getTotalDebt())
                .monthlyPayment(dto.getMonthlyPayment())
                .dueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusMonths(1))
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .build();
        return debtRepository.save(debt);
    }
}