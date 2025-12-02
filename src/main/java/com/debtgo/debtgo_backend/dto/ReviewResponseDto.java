package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private int rating;
    private String comment;

    private Long consultantId;
    private String consultantName;

    private Long entrepreneurId;
    private String entrepreneurName;

    private String reply;
    private String date;
}