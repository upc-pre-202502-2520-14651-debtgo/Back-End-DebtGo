package com.debtgo.debtgo_backend.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeMovementDto {
    private Date date;
    private String concept;
    private double amount;
    private String status; // COMPLETADO, PENDIENTE, etc.
}