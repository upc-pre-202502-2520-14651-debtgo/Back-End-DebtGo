package com.debtgo.debtgo_backend.service;


import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.review.Review;
import com.debtgo.debtgo_backend.dto.ReviewResponseDto;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ConsultantProfileRepository consultantRepo;

    @InjectMocks
    private ReviewService sut;

    private ConsultantProfile consultant;
    private Review review;

    @BeforeEach
    void setUp() {
        consultant = ConsultantProfile.builder()
                .id(1L)
                .name("John Doe")
                .skills("Finance, Negotiation")
                .rate(4.8)
                .build();

        review = Review.builder()
                .id(10L)
                .rating(5)
                .comment("Excelente servicio")
                .consultant(consultant)
                .build();
    }

    @Test
    void createReview_whenConsultantExists_returnsSavedReview() {
        // Arrange
        when(consultantRepo.findById(1L)).thenReturn(Optional.of(consultant));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        Review result = sut.createReview(100L, 1L, review);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Excelente servicio", result.getComment());
        assertEquals(1L, result.getConsultant().getId());
        verify(reviewRepository).save(review);
    }

    @Test
    void createReview_whenConsultantNotFound_throwsException() {
        // Arrange
        when(consultantRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.createReview(100L, 1L, review));
        assertTrue(ex.getMessage().contains("Consultant not found"));
    }

    @Test
    void getAllReviews_returnsListOfReviews() {
        // Arrange
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        // Act
        List<Review> result = sut.getAllReviews();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Excelente servicio", result.get(0).getComment());
        verify(reviewRepository).findAll();
    }

    @Test
    void addReply_whenReviewExists_updatesAndSavesReply() {
        // Arrange
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Review result = sut.addReply(10L, "Gracias por tu comentario");

        // Assert
        assertEquals("Gracias por tu comentario", result.getReply());
        verify(reviewRepository).save(review);
    }

    @Test
    void addReply_whenReviewNotFound_throwsException() {
        // Arrange
        when(reviewRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.addReply(10L, "reply"));
        assertTrue(ex.getMessage().contains("Review not found"));
    }

    @Test
    void getByConsultant_returnsReviewsForConsultant() {
        // Arrange
        when(reviewRepository.findByConsultant_Id(1L)).thenReturn(List.of(review));

        // Act
        List<Review> result = sut.getByConsultant(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Excelente servicio", result.get(0).getComment());
        verify(reviewRepository).findByConsultant_Id(1L);
    }

    @Test
    void toDto_convertsCorrectly() {
        // Act
        ReviewResponseDto dto = sut.toDto(review);

        // Assert
        assertEquals(10L, dto.getId());
        assertEquals(5, dto.getRating());
        assertEquals("Excelente servicio", dto.getComment());
        assertEquals(1L, dto.getConsultantId());
        assertEquals("John Doe", dto.getConsultantName());
    }

    @Test
    void toDto_whenConsultantNull_handlesGracefully() {
        // Arrange
        Review r = new Review();
        r.setId(20L);
        r.setRating(3);
        r.setComment("Regular");
        r.setConsultant(null);

        // Act
        ReviewResponseDto dto = sut.toDto(r);

        // Assert
        assertNull(dto.getConsultantId());
        assertNull(dto.getConsultantName());
    }
}
