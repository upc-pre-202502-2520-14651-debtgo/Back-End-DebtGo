package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthStatDto {
    private String nombre;
    private long valor;
}