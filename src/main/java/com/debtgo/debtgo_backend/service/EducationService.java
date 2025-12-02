package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.dto.EducationHighlightDto;
import com.debtgo.debtgo_backend.domain.education.EducationHighlight;
import com.debtgo.debtgo_backend.repository.EducationHighlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationHighlightRepository educationRepo;

    public List<EducationHighlightDto> listarRecursos() {
        return educationRepo.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public EducationHighlightDto obtenerPorId(Long id) {
        EducationHighlight e = educationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con ID: " + id));

        return convertirADto(e);
    }

    private EducationHighlightDto convertirADto(EducationHighlight e) {
        return new EducationHighlightDto(
                e.getId(),
                e.getCategory(),
                e.getTitle(),
                e.getLevel(),
                e.getPdfLink(),
                e.getVideoLink());
    }

    public List<String> listarVideos() {
        return educationRepo.findAll()
                .stream()
                .map(EducationHighlight::getVideoLink)
                .toList();
    }
}