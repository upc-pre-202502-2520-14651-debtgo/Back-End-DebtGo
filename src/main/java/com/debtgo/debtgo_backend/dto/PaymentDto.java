package com.debtgo.debtgo_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Double amount;
    private String method;
    private String status;
    private Long entrepreneurId;
    private Long consultantId;
}