package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.simulation.Simulation;
import com.debtgo.debtgo_backend.dto.simulation.*;
import com.debtgo.debtgo_backend.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository repo;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SimulationResponseDto preview(SimulationRequestDto dto) {
        return calculate(dto, false);
    }

    public SimulationResponseDto create(SimulationRequestDto dto) {
        SimulationResponseDto result = calculate(dto, true);
        // Guardar resumen
        Simulation s = Simulation.builder()
                .amount(dto.getAmount())
                .annualRate(dto.getAnnualRate())
                .months(dto.getMonths())
                .extra(dto.getExtra())
                .startDate(dto.getStartDate())
                .monthlyPayment(result.getMonthlyPayment())
                .totalInterest(result.getTotalInterest())
                .totalPayment(result.getTotalPayment())
                .userId(dto.getUserId())
                .build();
        s = repo.save(s);
        result.setSimulationId(s.getId());
        return result;
    }

    public List<Simulation> listByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    // --- core ---
    private SimulationResponseDto calculate(SimulationRequestDto dto, boolean normalize) {
        double P = dto.getAmount();
        double i = (dto.getAnnualRate() / 100.0) / 12.0;
        int n = dto.getMonths();
        double extra = Math.max(0, dto.getExtra() == 0 ? 0 : dto.getExtra());
        LocalDate fecha = dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now();

        double cuota = (i == 0) ? (P / n) : (P * i) / (1 - Math.pow(1 + i, -n));

        double balance = P;
        List<SimulationRowDto> rows = new ArrayList<>();
        double totalInteres = 0, totalPago = 0;
        int k = 1;

        while (balance > 0.005 && k <= 600) {
            double interes = balance * i;
            double pago = Math.min(balance + interes, cuota + extra);
            double capital = Math.min(balance, pago - interes);

            balance = balance - capital;
            totalInteres += interes;
            totalPago += pago;

            rows.add(new SimulationRowDto(
                    k,
                    fecha.format(F),
                    round(pago),
                    round(interes),
                    round(capital),
                    round(Math.max(0, balance))));

            fecha = fecha.plusMonths(1);
            k++;
        }

        return SimulationResponseDto.builder()
                .monthlyPayment(round(cuota))
                .totalInterest(round(totalInteres))
                .totalPayment(round(totalPago))
                .schedule(rows)
                .build();
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}