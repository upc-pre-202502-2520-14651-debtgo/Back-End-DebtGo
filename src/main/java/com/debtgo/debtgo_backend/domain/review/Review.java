package com.debtgo.debtgo_backend.domain.review;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
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

    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    private ConsultantProfile consultant;

    @Column(length = 1000)
    private String reply;
}