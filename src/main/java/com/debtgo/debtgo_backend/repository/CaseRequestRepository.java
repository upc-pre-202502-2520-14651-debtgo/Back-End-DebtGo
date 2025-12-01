package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.entity.CaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRequestRepository extends JpaRepository<CaseRequest, Long> {

    List<CaseRequest> findByConsultantId(Long consultantId);
}