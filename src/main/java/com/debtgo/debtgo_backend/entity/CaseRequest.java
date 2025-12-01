package com.debtgo.debtgo_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "case_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long consultantId;
    private Long entrepreneurId;

    private String title;
    private String description;

    private String status; // PENDING, ACCEPTED, REJECTED
}