package com.debtgo.debtgo_backend.domain.simulation;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "simulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Double annualRate;
    private Integer months;
    private Double extra;
    private LocalDate startDate;

    // KPIs guardados para listar rápido
    private Double monthlyPayment;
    private Double totalInterest;
    private Double totalPayment;

    // Relación simple al usuario
    private Long userId;
}