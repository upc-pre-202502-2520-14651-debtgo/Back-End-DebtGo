package com.debtgo.debtgo_backend.repository;

import com.debtgo.debtgo_backend.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByConsultant_Id(Long consultantId);
}
