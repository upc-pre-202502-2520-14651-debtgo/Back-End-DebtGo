package com.debtgo.debtgo_backend.dto;

import com.debtgo.debtgo_backend.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int rating; // de 1 a 5
    private String comment;

    // Convierte el DTO en entidad
    public Review toEntity() {
        Review review = new Review();
        review.setRating(this.rating);
        review.setComment(this.comment);
        return review;
    }
}
