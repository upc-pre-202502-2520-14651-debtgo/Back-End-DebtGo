package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.ConsultantRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantRequestRepository extends JpaRepository<ConsultantRequest, Long> {

    @Query("SELECT r FROM ConsultantRequest r WHERE r.consultant.id = :consultantId ORDER BY r.requestedAt DESC")
    List<ConsultantRequest> findByConsultantId(@Param("consultantId") Long consultantId);
}