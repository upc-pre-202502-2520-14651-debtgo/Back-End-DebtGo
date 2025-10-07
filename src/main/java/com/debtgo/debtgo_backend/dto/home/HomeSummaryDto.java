package com.debtgo.debtgo_backend.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeSummaryDto {
    private int debtsActive;
    private double totalPending;
    private int upcomingPayments;
    private int alerts;
}