package com.debtgo.debtgo_backend.dto.debt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebtSummaryDto {
    private Long id;
    private String clientName;
    private Double totalDebt;
    private Double monthlyPayment;
    private LocalDate dueDate;
    private String status;
}