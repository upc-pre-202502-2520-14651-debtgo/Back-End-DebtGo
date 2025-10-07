package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private int rating;
    private String comment;
    private Long consultantId;
    private String consultantName;
}