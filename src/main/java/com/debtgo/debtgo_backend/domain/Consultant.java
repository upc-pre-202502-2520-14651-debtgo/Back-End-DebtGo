package com.debtgo.debtgo_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "consultants")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String fullName;
    private String specialty;
    private String experience;

    @Column(length = 2000)
    private String description;
    private String profileImage;
    private Double rating;
    private Double hourlyRate;
}