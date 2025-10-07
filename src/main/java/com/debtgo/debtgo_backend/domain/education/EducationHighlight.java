package com.debtgo.debtgo_backend.domain.education;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "education_highlights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(nullable = false, length = 40)
    private String level;

    @Column(nullable = false, length = 255)
    private String link;

    @Column(nullable = false, length = 255)
    private String pdfLink;

    @Column(nullable = false, length = 255)
    private String videoLink;

}