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
public class ConsultantDto implements Serializable {
    private Long id;
    private String fullName;
    private String specialty;
    private String experience;
    private String description;
    private String profileImage;
    private Double rating;
    private Double hourlyRate;
}