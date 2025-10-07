package com.debtgo.debtgo_backend.dto.simulation;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SimulationRequestDto {
    private double amount; // monto
    private double annualRate; // % anual
    private int months; // plazo meses
    private double extra; // pago extra mensual
    private LocalDate startDate; // fecha inicio
    private Long userId; // para guardar por usuario
}