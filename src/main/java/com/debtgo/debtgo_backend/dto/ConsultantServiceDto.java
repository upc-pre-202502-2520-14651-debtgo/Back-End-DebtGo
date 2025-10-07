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
public class ConsultantServiceDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long consultantId;
}