package com.debtgo.debtgo_backend.domain.review;

import java.time.LocalDateTime;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.profile.EntrepreneurProfile;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1..5
    private int rating;

    @Column(length = 1000)
    private String comment;

    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private ConsultantProfile consultant;

    @ManyToOne
    @JoinColumn(name = "entrepreneur_id")
    private EntrepreneurProfile entrepreneur;

    @Column(length = 1000)
    private String reply;
}