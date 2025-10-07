package com.debtgo.debtgo_backend.domain.advisory;

import java.time.LocalDateTime;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "advisories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advisory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private AdvisoryStatus status = AdvisoryStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private ConsultantProfile consultant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}