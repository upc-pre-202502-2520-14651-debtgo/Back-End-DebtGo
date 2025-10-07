package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.education.EducationHighlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationHighlightRepository extends JpaRepository<EducationHighlight, Long> {
}