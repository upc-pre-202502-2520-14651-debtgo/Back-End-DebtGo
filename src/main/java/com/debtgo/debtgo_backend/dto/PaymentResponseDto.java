package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Double amount;
    private String method;
    private String status;
    private Long consultantId;
}