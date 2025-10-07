package com.debtgo.debtgo_backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultant_services")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;
}