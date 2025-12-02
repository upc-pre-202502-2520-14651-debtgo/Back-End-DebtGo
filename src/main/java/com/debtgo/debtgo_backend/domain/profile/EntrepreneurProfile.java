package com.debtgo.debtgo_backend.domain.profile;

import com.debtgo.debtgo_backend.domain.review.Review;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class EntrepreneurProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // nombre del emprendedor
    private String email; // email (para mostrar en reseñas)
    private String phone; // opcional

    @OneToMany(mappedBy = "entrepreneur")
    private List<Review> reviews;
}