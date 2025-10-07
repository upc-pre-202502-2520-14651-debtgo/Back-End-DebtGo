package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConsultantProfileRepository extends JpaRepository<ConsultantProfile, Long> {

    Optional<ConsultantProfile> findByUser(User user);

    Optional<ConsultantProfile> findByUser_Email(String email);
}