package com.debtgo.debtgo_backend.dto.simulation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResponseDto {

    private Long simulationId;
    private double monthlyPayment;
    private double totalInterest;
    private double totalPayment;
    private String userName;
    private List<SimulationRowDto> schedule;
}