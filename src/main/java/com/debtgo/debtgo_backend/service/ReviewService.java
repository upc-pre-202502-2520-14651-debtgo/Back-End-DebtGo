package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.dto.ReviewResponseDto;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConsultantProfileRepository consultantRepo;

    public ReviewService(ReviewRepository reviewRepository,
            ConsultantProfileRepository consultantRepo) {
        this.reviewRepository = reviewRepository;
        this.consultantRepo = consultantRepo;
    }

    public Review createReview(Long entrepreneurId, Long consultantId, Review review) {
        ConsultantProfile con = consultantRepo.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        review.setConsultant(con);
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public ReviewResponseDto toDto(Review r) {
        return new ReviewResponseDto(
                r.getId(),
                r.getRating(),
                r.getComment(),
                r.getConsultant() != null ? r.getConsultant().getId() : null,
                r.getConsultant() != null ? r.getConsultant().getName() : null);
    }

    public Review addReply(Long id, String reply) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        r.setReply(reply);
        return reviewRepository.save(r);
    }

    public List<Review> getByConsultant(Long consultantId) {
        return reviewRepository.findByConsultant_Id(consultantId);
    }
}