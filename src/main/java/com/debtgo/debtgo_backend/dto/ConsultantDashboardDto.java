package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantDashboardDto {
    private double promedioCalificacion;
    private int totalCasos;
    private double satisfaccion;
    private int totalResenas;
    private List<MonthStatDto> meses;
    private List<ReviewResponseDto> ultimasResenas;
}