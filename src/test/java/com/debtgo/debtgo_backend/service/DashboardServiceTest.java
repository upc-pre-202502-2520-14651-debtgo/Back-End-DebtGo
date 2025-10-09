package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.dto.ConsultantDashboardDto;
import com.debtgo.debtgo_backend.dto.ReviewResponseDto;
import com.debtgo.debtgo_backend.repository.AdvisoryRepository;
import com.debtgo.debtgo_backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private AdvisoryRepository advisoryRepo;

    @InjectMocks
    private DashboardService sut;

    private ConsultantProfile consultant;

    @BeforeEach
    void setUp() {
        consultant = ConsultantProfile.builder()
                .id(1L)
                .name("Carlos")
                .build();
    }

    @Test
    void getConsultantDashboard_whenHasData_returnsExpectedStats() {
        // Arrange
        var reviews = List.of(
                Review.builder().id(1L).rating(5).comment("Excelente").consultant(consultant).build(),
                Review.builder().id(2L).rating(4).comment("Muy bueno").consultant(consultant).build()
        );

        var advisories = List.of(
                Advisory.builder().id(1L).title("Caso 1").consultant(consultant)
                        .createdAt(LocalDateTime.of(2025, Month.JANUARY, 10, 10, 0)).build(),
                Advisory.builder().id(2L).title("Caso 2").consultant(consultant)
                        .createdAt(LocalDateTime.of(2025, Month.FEBRUARY, 15, 12, 0)).build(),
                Advisory.builder().id(3L).title("Caso 3").consultant(consultant)
                        .createdAt(LocalDateTime.of(2025, Month.FEBRUARY, 20, 9, 0)).build()
        );

        when(reviewRepo.findByConsultant_Id(1L)).thenReturn(reviews);
        when(advisoryRepo.findByConsultant_Id(1L)).thenReturn(advisories);

        // Act
        ConsultantDashboardDto result = sut.getConsultantDashboard(1L);

        // Assert
        assertNotNull(result);
        assertEquals(4.5, result.getPromedioCalificacion(), 0.001, "El promedio de calificación debe ser 4.5");
        assertEquals(3, result.getTotalCasos(), "Debe tener 3 asesorías");
        assertEquals(2, result.getTotalResenas(), "Debe tener 2 reseñas");
        assertEquals(90.0, result.getSatisfaccion(), 0.01, "Satisfacción debe ser promedio*20");

        // Meses agrupados (Jan -> 1, Feb -> 2)
        assertEquals(2, result.getMeses().size());

        // Últimas reseñas
        assertEquals(2, result.getUltimasResenas().size());
        ReviewResponseDto latest = result.getUltimasResenas().get(0);
        assertEquals(2L, latest.getId());
        assertEquals("Muy bueno", latest.getComment());
    }

    @Test
    void getConsultantDashboard_whenNoReviewsOrAdvisories_returnsZeros() {
        // Arrange
        when(reviewRepo.findByConsultant_Id(1L)).thenReturn(List.of());
        when(advisoryRepo.findByConsultant_Id(1L)).thenReturn(List.of());

        // Act
        ConsultantDashboardDto result = sut.getConsultantDashboard(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getPromedioCalificacion());
        assertEquals(0.0, result.getSatisfaccion());
        assertEquals(0, result.getTotalCasos());
        assertEquals(0, result.getTotalResenas());
        assertTrue(result.getMeses().isEmpty());
        assertTrue(result.getUltimasResenas().isEmpty());
    }

    @Test
    void getConsultantDashboard_limitsLastReviewsToFour() {
        // Arrange
        var reviews = List.of(
                Review.builder().id(10L).rating(5).comment("R1").consultant(consultant).build(),
                Review.builder().id(11L).rating(4).comment("R2").consultant(consultant).build(),
                Review.builder().id(12L).rating(3).comment("R3").consultant(consultant).build(),
                Review.builder().id(13L).rating(2).comment("R4").consultant(consultant).build(),
                Review.builder().id(14L).rating(1).comment("R5").consultant(consultant).build()
        );

        when(reviewRepo.findByConsultant_Id(1L)).thenReturn(reviews);
        when(advisoryRepo.findByConsultant_Id(1L)).thenReturn(List.of());

        // Act
        ConsultantDashboardDto result = sut.getConsultantDashboard(1L);

        // Assert
        assertEquals(4, result.getUltimasResenas().size(), "Solo debe devolver 4 reseñas más recientes");
        assertEquals(14L, result.getUltimasResenas().get(0).getId(), "La reseña más reciente debe tener ID mayor");
    }

}
