package com.debtgo.debtgo_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantProfileDto {
    private Long id;
    private String name;
    private String skills;
    private Double rate;
}