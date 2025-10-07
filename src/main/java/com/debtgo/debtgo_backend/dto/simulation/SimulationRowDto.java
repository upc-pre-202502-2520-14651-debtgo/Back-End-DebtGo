package com.debtgo.debtgo_backend.dto.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationRowDto {
    private int n;
    private String date;
    private double payment;
    private double interest;
    private double principal;
    private double balance;
}