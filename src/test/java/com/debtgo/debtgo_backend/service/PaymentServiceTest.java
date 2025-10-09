package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.payment.Payment;
import com.debtgo.debtgo_backend.domain.payment.PaymentStatus;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.dto.PaymentDto;
import com.debtgo.debtgo_backend.dto.PaymentResponseDto;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.PaymentRepository;
import com.debtgo.debtgo_backend.repository.UserRepository;
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
public class PaymentServiceTest {


    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConsultantProfileRepository consultantRepo;

    @InjectMocks
    private PaymentService sut;

    private User consultantUser;
    private ConsultantProfile consultantProfile;
    private PaymentDto dto;
    private Payment payment;

    @BeforeEach
    void setUp() {
        consultantUser = new User("consultant@mail.com", "pass", "CONSULTANT");
        consultantUser.setId(1L);

        consultantProfile = new ConsultantProfile();
        consultantProfile.setId(100L);

        dto = new PaymentDto();
        dto.setAmount(500.0);
        dto.setMethod("CARD");
        dto.setStatus("COMPLETED");

        payment = Payment.builder()
                .id(200L)
                .amount(500.0)
                .method("CARD")
                .status(PaymentStatus.COMPLETED)
                .consultant(consultantProfile)
                .build();
    }

    @Test
    void createPayment_whenConsultant_createsSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(consultantUser));
        when(consultantRepo.findByUser(consultantUser)).thenReturn(Optional.of(consultantProfile));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        PaymentResponseDto result = sut.createPayment(1L, dto);

        // Assert
        assertNotNull(result);
        assertEquals(200L, result.getId());
        assertEquals(500.0, result.getAmount());
        assertEquals("CARD", result.getMethod());
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(100L, result.getConsultantId());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void createPayment_whenUserNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.createPayment(1L, dto));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void createPayment_whenConsultantProfileMissing_throwsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(consultantUser));
        when(consultantRepo.findByUser(consultantUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.createPayment(1L, dto));
        assertTrue(ex.getMessage().contains("Consultant profile not found"));
    }

    @Test
    void createPayment_whenUnsupportedRole_throwsException() {
        // Arrange
        User entrepreneur = new User("ent@mail.com", "pass", "ENTREPRENEUR");
        entrepreneur.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(entrepreneur));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.createPayment(2L, dto));
        assertTrue(ex.getMessage().contains("Unsupported user role"));
    }

    @Test
    void listForUser_whenConsultant_returnsPayments() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(consultantUser));
        when(consultantRepo.findByUser(consultantUser)).thenReturn(Optional.of(consultantProfile));
        when(paymentRepository.findByConsultant_Id(100L)).thenReturn(List.of(payment));

        // Act
        List<PaymentResponseDto> result = sut.listForUser(1L);

        // Assert
        assertEquals(1, result.size());
        PaymentResponseDto dto = result.get(0);
        assertEquals(200L, dto.getId());
        assertEquals("COMPLETED", dto.getStatus());
        assertEquals(100L, dto.getConsultantId());
        verify(paymentRepository).findByConsultant_Id(100L);
    }

    @Test
    void listForUser_whenUserNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.listForUser(99L));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void listForUser_whenConsultantProfileMissing_throwsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(consultantUser));
        when(consultantRepo.findByUser(consultantUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.listForUser(1L));
        assertTrue(ex.getMessage().contains("Consultant profile not found"));
    }
}
