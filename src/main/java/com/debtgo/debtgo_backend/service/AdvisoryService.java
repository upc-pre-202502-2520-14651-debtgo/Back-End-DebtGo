package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.domain.advisory.AdvisoryStatus;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.dto.AdvisoryResponseDto;
import com.debtgo.debtgo_backend.repository.AdvisoryRepository;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepo;
    private final ConsultantProfileRepository consultantRepo;

    public AdvisoryService(AdvisoryRepository advisoryRepo,
            ConsultantProfileRepository consultantRepo) {
        this.advisoryRepo = advisoryRepo;
        this.consultantRepo = consultantRepo;
    }

    /**
     * Crear una nueva asesoría (pendiente por defecto).
     */
    public Advisory createAdvisory(Long consultantId, Advisory advisory) {
        advisory.setStatus(AdvisoryStatus.PENDING);
        if (consultantId != null) {
            ConsultantProfile consultant = consultantRepo.findById(consultantId)
                    .orElseThrow(() -> new RuntimeException("Consultant not found"));
            advisory.setConsultant(consultant);
        }
        return advisoryRepo.save(advisory);
    }

    /**
     * Listar todas las asesorías existentes.
     */
    public List<Advisory> getAll() {
        return advisoryRepo.findAll();
    }

    /**
     * Asignar un consultor a una asesoría existente.
     */
    public Optional<Advisory> assignConsultant(Long advisoryId, Long consultantId) {
        Advisory advisory = advisoryRepo.findById(advisoryId)
                .orElseThrow(() -> new RuntimeException("Advisory not found"));
        ConsultantProfile consultant = consultantRepo.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));
        advisory.setConsultant(consultant);
        advisory.setStatus(AdvisoryStatus.ACCEPTED);
        return Optional.of(advisoryRepo.save(advisory));
    }

    /**
     * Convertir entidad Advisory a DTO.
     */
    public AdvisoryResponseDto toDto(Advisory advisory) {
        return new AdvisoryResponseDto(
                advisory.getId(),
                advisory.getTitle(),
                advisory.getDescription(),
                advisory.getStatus().name(),
                advisory.getConsultant() != null ? advisory.getConsultant().getId() : null);
    }
}