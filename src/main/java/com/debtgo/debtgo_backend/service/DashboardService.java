package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.dto.*;
import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.repository.AdvisoryRepository;
import com.debtgo.debtgo_backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ReviewRepository reviewRepo;
    private final AdvisoryRepository advisoryRepo;

    public DashboardService(ReviewRepository reviewRepo, AdvisoryRepository advisoryRepo) {
        this.reviewRepo = reviewRepo;
        this.advisoryRepo = advisoryRepo;
    }

    public ConsultantDashboardDto getConsultantDashboard(Long consultantId) {
        // 🔹 Obtener reseñas del consultor
        List<Review> reviews = reviewRepo.findByConsultant_Id(consultantId);
        double promedio = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        int totalResenas = reviews.size();

        // 🔹 Obtener asesorías reales del consultor
        List<Advisory> asesorias = advisoryRepo.findByConsultant_Id(consultantId);
        int totalCasos = asesorias.size();

        // 🔹 Calcular satisfacción (porcentaje)
        double satisfaccion = Math.min(100, promedio * 20);

        // 🔹 Agrupar asesorías por mes (basado en fecha de creación)
        Map<String, Long> porMes = asesorias.stream()
                .filter(a -> a.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getCreatedAt().getMonth().toString(),
                        Collectors.counting()));

        List<MonthStatDto> meses = porMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new MonthStatDto(e.getKey().substring(0, 3), e.getValue()))
                .toList();

        // 🔹 Últimas reseñas
        List<ReviewResponseDto> ultimas = reviews.stream()
                .sorted(Comparator.comparing(Review::getId).reversed())
                .limit(4)
                .map(r -> new ReviewResponseDto(
                        r.getId(),
                        r.getRating(),
                        r.getComment(),
                        r.getConsultant() != null ? r.getConsultant().getId() : null,
                        r.getConsultant() != null ? r.getConsultant().getName() : null))
                .toList();

        return new ConsultantDashboardDto(promedio, totalCasos, satisfaccion, totalResenas, meses, ultimas);
    }
}