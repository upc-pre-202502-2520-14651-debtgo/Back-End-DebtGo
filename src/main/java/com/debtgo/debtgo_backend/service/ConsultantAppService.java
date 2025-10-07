package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.repository.ConsultantRepository;
import com.debtgo.debtgo_backend.repository.ConsultantRequestRepository;
import com.debtgo.debtgo_backend.repository.ConsultantServiceRepository;
import com.debtgo.debtgo_backend.dto.ConsultantDto;
import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.dto.ConsultantSummaryDto;
import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.ConsultantServiceEntity;
import com.debtgo.debtgo_backend.domain.ConsultantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConsultantAppService {

    private final ConsultantRepository consultantRepo;
    private final ConsultantServiceRepository serviceRepo;
    private final ConsultantRequestRepository requestRepo;

    // PERFIL
    public ConsultantDto getConsultant(Long id) {
        Consultant c = consultantRepo.findById(id).orElseThrow(() -> new RuntimeException("Consultor no encontrado"));
        return map(c);
    }

    public ConsultantDto updateConsultant(Long id, ConsultantDto dto) {
        Consultant c = consultantRepo.findById(id).orElseThrow(() -> new RuntimeException("Consultor no encontrado"));
        c.setFullName(dto.getFullName());
        c.setSpecialty(dto.getSpecialty());
        c.setExperience(dto.getExperience());
        c.setDescription(dto.getDescription());
        c.setHourlyRate(dto.getHourlyRate());
        c.setProfileImage(dto.getProfileImage());
        c.setRating(dto.getRating());
        return map(consultantRepo.save(c));
    }

    // RESUMEN
    public ConsultantSummaryDto getSummary(Long id) {
        long services = serviceRepo.findByConsultantId(id).size();
        List<ConsultantRequest> cases = requestRepo.findByConsultantId(id);
        long active = cases.stream().filter(r -> "IN_PROGRESS".equals(r.getStatus())).count();
        long served = cases.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
        double rating = consultantRepo.findById(id).map(Consultant::getRating).orElse(0.0);

        return ConsultantSummaryDto.builder()
                .servedClients(served)
                .activeAdvisories(active)
                .publishedServices(services)
                .avgRating(rating != 0.0 ? rating : 4.5)
                .build();
    }

    // SERVICIOS
    public List<ConsultantServiceDto> byConsultant(Long id) {
        return serviceRepo.findByConsultantId(id)
                .stream()
                .map(this::mapService)
                .toList();
    }

    public ConsultantServiceDto createService(ConsultantServiceDto dto) {
        Consultant c = consultantRepo.findById(dto.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultor no encontrado"));
        ConsultantServiceEntity s = ConsultantServiceEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .consultant(c)
                .build();
        return mapService(serviceRepo.save(s));
    }

    public ConsultantServiceDto updateService(Long id, ConsultantServiceDto dto) {
        ConsultantServiceEntity s = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setPrice(dto.getPrice());
        return mapService(serviceRepo.save(s));
    }

    public void deleteService(Long id) {
        serviceRepo.deleteById(id);
    }

    // CASOS
    public List<ConsultantRequest> cases(Long consultantId) {
        return requestRepo.findByConsultantId(consultantId);
    }

    public void updateCaseStatus(Long id, String status) {
        ConsultantRequest r = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caso no encontrado"));
        r.setStatus(status);
        requestRepo.save(r);
    }

    // MÉTRICAS
    public Map<String, Object> metrics(Long id) {
        List<ConsultantRequest> list = requestRepo.findByConsultantId(id);
        long sessions = list.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
        double revenue = serviceRepo.findByConsultantId(id)
                .stream()
                .mapToDouble(ConsultantServiceEntity::getPrice)
                .sum();
        double avgRating = consultantRepo.findById(id).map(Consultant::getRating).orElse(4.5);
        double compliance = list.isEmpty() ? 0 : (sessions * 100.0 / list.size());

        return Map.of(
                "sessions", sessions,
                "revenue", revenue,
                "avgRating", avgRating,
                "compliance", Math.round(compliance * 100.0) / 100.0);
    }

    private ConsultantDto map(Consultant c) {
        return ConsultantDto.builder()
                .id(c.getId())
                .fullName(c.getFullName())
                .specialty(c.getSpecialty())
                .experience(c.getExperience())
                .description(c.getDescription())
                .profileImage(c.getProfileImage())
                .rating(c.getRating())
                .hourlyRate(c.getHourlyRate())
                .build();
    }

    private ConsultantServiceDto mapService(ConsultantServiceEntity s) {
        return ConsultantServiceDto.builder()
                .id(s.getId())
                .title(s.getTitle())
                .description(s.getDescription())
                .price(s.getPrice())
                .consultantId(s.getConsultant().getId())
                .build();
    }

    public Consultant createConsultant(Consultant consultant) {
        return consultantRepo.save(consultant);
    }

    public Consultant findConsultantById(Long id) {
        return consultantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));
    }

}