package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.profile.EntrepreneurProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepreneurProfileRepository extends JpaRepository<EntrepreneurProfile, Long> {
}