package com.debtgo.debtgo_backend.dto.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeEducationDto {
    private Long id;
    private String title;
    private String category;
    private String level;
    private String pdfLink;
    private String videoLink;
}