package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {
    List<Advisory> findByConsultant_Id(Long consultantId);
}