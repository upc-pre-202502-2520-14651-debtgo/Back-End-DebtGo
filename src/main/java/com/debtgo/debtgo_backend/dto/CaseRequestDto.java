package com.debtgo.debtgo_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseRequestDto {

    private Long id;
    private Long consultantId;
    private Long entrepreneurId;

    private String title;
    private String description;

    private String status;
}