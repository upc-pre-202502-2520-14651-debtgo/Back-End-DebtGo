package com.debtgo.debtgo_backend.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationHighlightDto {
    private Long id;
    private String category;
    private String title;
    private String level;
    private String pdfLink;
    private String videoLink;
}