package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.dto.CaseRequestDto;
import com.debtgo.debtgo_backend.repository.CaseRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseRequestService {

    private final CaseRequestRepository repo;

    public List<CaseRequestDto> byConsultant(Long id) {

        return repo.findByConsultantId(id)
                .stream()
                .map(r -> new CaseRequestDto(
                        r.getId(),
                        r.getConsultantId(),
                        r.getEntrepreneurId(),
                        r.getTitle(),
                        r.getDescription(),
                        r.getStatus()))
                .toList();
    }
}