package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.ConsultantServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantServiceRepository extends JpaRepository<ConsultantServiceEntity, Long> {

    @Query("SELECT s FROM ConsultantServiceEntity s WHERE s.consultant.id = :consultantId")
    List<ConsultantServiceEntity> findByConsultantId(@Param("consultantId") Long consultantId);
}