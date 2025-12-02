package com.debtgo.debtgo_backend.dto;

import com.debtgo.debtgo_backend.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int rating; // 1 a 5
    private String comment;

    public Review toEntity() {
        Review r = new Review();
        r.setRating(rating);
        r.setComment(comment);
        return r;
    }
}