package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.PaymentDto;
import com.debtgo.debtgo_backend.dto.PaymentResponseDto;
import com.debtgo.debtgo_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Crea un pago indicando el id del usuario (consultor o emprendedor)
     */
    @PostMapping("/{userId}")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @PathVariable Long userId,
            @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(userId, dto));
    }

    /**
     * Lista pagos de un usuario específico (según su rol)
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> listPayments(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.listForUser(userId));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/by-consultant/{id}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByConsultant(@PathVariable Long id) {
        var payments = paymentService.listForUser(id);
        return ResponseEntity.ok(payments);
    }

}