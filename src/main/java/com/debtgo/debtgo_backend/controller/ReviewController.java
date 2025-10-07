package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.dto.ReviewDto;
import com.debtgo.debtgo_backend.dto.ReviewResponseDto;
import com.debtgo.debtgo_backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestParam Long entrepreneurId,
            @RequestParam Long consultantId,
            @RequestBody ReviewDto dto) {
        Review saved = reviewService.createReview(entrepreneurId, consultantId, dto.toEntity());
        return ResponseEntity.ok(reviewService.toDto(saved));
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<ReviewResponseDto> replyToReview(
            @PathVariable Long id,
            @RequestBody String reply) {
        Review updated = reviewService.addReply(id, reply);
        return ResponseEntity.ok(reviewService.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(
                reviewService.getAllReviews()
                        .stream()
                        .map(reviewService::toDto)
                        .toList());
    }

    @GetMapping("/consultant/{consultantId}")
    public ResponseEntity<List<ReviewResponseDto>> getByConsultant(@PathVariable Long consultantId) {
        return ResponseEntity.ok(
                reviewService.getByConsultant(consultantId)
                        .stream()
                        .map(reviewService::toDto)
                        .toList());
    }

}