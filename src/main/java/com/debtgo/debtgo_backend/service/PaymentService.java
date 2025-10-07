package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.domain.payment.Payment;
import com.debtgo.debtgo_backend.domain.payment.PaymentStatus;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.dto.PaymentDto;
import com.debtgo.debtgo_backend.dto.PaymentResponseDto;
import com.debtgo.debtgo_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ConsultantProfileRepository consultantRepo;

    /**
     * Crea un pago y asociarlo al perfil correcto dependiendo del rol del usuario.
     * En Swagger se pasará el userId manualmente (en lugar de depender del token).
     */
    public PaymentResponseDto createPayment(Long userId, PaymentDto dto) {
        // Busca el usuario base
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus() != null
                ? PaymentStatus.valueOf(dto.getStatus().toUpperCase())
                : PaymentStatus.PENDING);

        // Asocia al perfil según el rol
        if ("CONSULTANT".equalsIgnoreCase(user.getRole())) {
            ConsultantProfile con = consultantRepo.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Consultant profile not found for user " + userId));
            payment.setConsultant(con);
        } else {
            throw new RuntimeException("Unsupported user role for payment creation");
        }

        Payment saved = paymentRepository.save(payment);
        return toDto(saved);
    }

    /**
     * Lista todos los pagos creados por un usuario, detectando su perfil.
     */
    public List<PaymentResponseDto> listForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        List<Payment> payments;

        if ("CONSULTANT".equalsIgnoreCase(user.getRole())) {
            ConsultantProfile con = consultantRepo.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Consultant profile not found for user " + userId));
            payments = paymentRepository.findByConsultant_Id(con.getId());
        } else {
            throw new RuntimeException("Unsupported user role for payment listing");
        }

        return payments.stream().map(this::toDto).toList();
    }

    private PaymentResponseDto toDto(Payment p) {
        return new PaymentResponseDto(
                p.getId(),
                p.getAmount(),
                p.getMethod(),
                p.getStatus().name(),
                p.getConsultant() != null ? p.getConsultant().getId() : null);
    }
}