package com.debtgo.debtgo_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EducationHighlightDto {
    private Long id;
    private String category;
    private String title;
    private String level;
    private String pdfLink;
    private String videoLink;
}