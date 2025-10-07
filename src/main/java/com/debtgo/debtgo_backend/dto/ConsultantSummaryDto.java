package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultantSummaryDto implements Serializable {
    private long servedClients;
    private long activeAdvisories;
    private long publishedServices;
    private double avgRating;
}