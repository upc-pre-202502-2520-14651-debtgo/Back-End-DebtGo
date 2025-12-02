package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.profile.EntrepreneurProfile;
import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.dto.ReviewResponseDto;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.EntrepreneurProfileRepository;
import com.debtgo.debtgo_backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConsultantProfileRepository consultantRepo;
    private final EntrepreneurProfileRepository entrepreneurRepo;

    public ReviewService(
            ReviewRepository reviewRepository,
            ConsultantProfileRepository consultantRepo,
            EntrepreneurProfileRepository entrepreneurRepo) {
        this.reviewRepository = reviewRepository;
        this.consultantRepo = consultantRepo;
        this.entrepreneurRepo = entrepreneurRepo;
    }

    // Crear reseña
    public Review createReview(Long entrepreneurId, Long consultantId, Review review) {

        ConsultantProfile con = consultantRepo.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        EntrepreneurProfile ent = entrepreneurRepo.findById(entrepreneurId)
                .orElseThrow(() -> new RuntimeException("Entrepreneur not found"));

        review.setConsultant(con);
        review.setEntrepreneur(ent);

        return reviewRepository.save(review);
    }

    // Convertir a DTO
    public ReviewResponseDto toDto(Review r) {

        String formattedDate = r.getDate() != null
                ? r.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : null;

        return new ReviewResponseDto(
                r.getId(),
                r.getRating(),
                r.getComment(),

                r.getConsultant() != null ? r.getConsultant().getId() : null,
                r.getConsultant() != null ? r.getConsultant().getName() : null,

                r.getEntrepreneur() != null ? r.getEntrepreneur().getId() : null,
                r.getEntrepreneur() != null ? r.getEntrepreneur().getName() : null,

                r.getReply(),
                formattedDate);
    }

    public Review addReply(Long id, String reply) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        r.setReply(reply);
        return reviewRepository.save(r);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getByConsultant(Long consultantId) {
        return reviewRepository.findByConsultant_Id(consultantId);
    }
}