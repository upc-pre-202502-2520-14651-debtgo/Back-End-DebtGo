package com.debtgo.debtgo_backend.domain.debt;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String clientName;

    @Column(nullable = false)
    private Double totalDebt;

    @Column(nullable = false)
    private Double monthlyPayment;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, length = 20)
    private String status;
}