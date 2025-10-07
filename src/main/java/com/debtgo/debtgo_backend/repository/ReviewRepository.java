package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByConsultant_Id(Long consultantId);
}