package com.debtgo.debtgo_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultant_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;
    private LocalDateTime requestedAt;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, REJECTED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;
}