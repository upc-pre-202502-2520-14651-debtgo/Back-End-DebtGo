package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
}