package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.dto.ConsultantProfileDto;
import com.debtgo.debtgo_backend.service.ConsultantProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultants")
public class ConsultantProfileController {

    private final ConsultantProfileService consultantService;

    public ConsultantProfileController(ConsultantProfileService consultantService) {
        this.consultantService = consultantService;
    }

    @PostMapping("/profile/{email}")
    public ResponseEntity<?> createProfile(@PathVariable String email,
            @RequestBody ConsultantProfileDto dto) {
        ConsultantProfile profile = new ConsultantProfile();
        profile.setName(dto.getName());
        profile.setSkills(dto.getSkills());
        profile.setRate(dto.getRate());

        ConsultantProfile saved = consultantService.createProfile(email, profile);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<?> getProfileByUser(@PathVariable String email) {
        return consultantService.getProfileByUser(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}