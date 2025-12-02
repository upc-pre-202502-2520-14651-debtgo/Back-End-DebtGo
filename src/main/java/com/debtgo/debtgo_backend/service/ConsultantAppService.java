package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.repository.ConsultantRepository;
import com.debtgo.debtgo_backend.repository.ConsultantRequestRepository;
import com.debtgo.debtgo_backend.repository.ConsultantServiceRepository;

import com.debtgo.debtgo_backend.dto.ConsultantDto;
import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.dto.ConsultantSummaryDto;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.ConsultantRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantAppService {

        private final ConsultantRepository consultantRepository;
        private final ConsultantServiceRepository consultantServiceRepository;
        private final ConsultantRequestRepository requestRepo;

        public ConsultantDto getConsultant(Long id) {
                Consultant c = consultantRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Consultor no encontrado"));
                return map(c);
        }

        public ConsultantSummaryDto getSummary(Long id) {

                long services = consultantServiceRepository.findByConsultantId(id).size();
                List<ConsultantRequest> cases = requestRepo.findByConsultantId(id);

                long active = cases.stream().filter(r -> "IN_PROGRESS".equals(r.getStatus())).count();
                long served = cases.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();

                double rating = consultantRepository.findById(id)
                                .map(Consultant::getRating)
                                .orElse(4.5);

                return ConsultantSummaryDto.builder()
                                .servedClients(served)
                                .activeAdvisories(active)
                                .publishedServices(services)
                                .avgRating(rating)
                                .build();
        }

        public List<ConsultantServiceDto> byConsultant(Long id) {
                return getServices(id);
        }

        public List<ConsultantServiceDto> getServices(Long consultantId) {
                return consultantServiceRepository.findByConsultantId(consultantId)
                                .stream()
                                .map(s -> new ConsultantServiceDto(
                                                s.getId(),
                                                s.getConsultant().getId(),
                                                s.getTitle(),
                                                s.getDescription(),
                                                s.getPrice()))
                                .toList();
        }

        public List<ConsultantDto> findAll() {
                return consultantRepository.findAll()
                                .stream()
                                .map(this::map)
                                .toList();
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
}