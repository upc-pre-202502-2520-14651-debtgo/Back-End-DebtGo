package com.debtgo.debtgo_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricsDto {

    private int totalCases;
    private int resolvedCases;
    private double satisfaction;
}