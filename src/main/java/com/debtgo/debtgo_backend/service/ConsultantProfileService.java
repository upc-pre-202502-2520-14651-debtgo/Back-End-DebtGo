package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsultantProfileService {

    private final ConsultantProfileRepository consultantRepo;
    private final UserRepository userRepo;

    public ConsultantProfileService(ConsultantProfileRepository consultantRepo,
            UserRepository userRepo) {
        this.consultantRepo = consultantRepo;
        this.userRepo = userRepo;
    }

    public ConsultantProfile createProfile(String email, ConsultantProfile profile) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        profile.setUser(user);
        return consultantRepo.save(profile);
    }

    public Optional<ConsultantProfile> getProfileByUser(String email) {
        return consultantRepo.findByUser_Email(email);
    }
}