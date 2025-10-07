package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.home.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "http://localhost:4200") 
public class HomeController {

    @GetMapping("/summary")
    public HomeSummaryDto getSummary() {
        return new HomeSummaryDto(
                3, // deudas activas
                4500.75, // total pendiente
                2, // próximos pagos
                1 // alertas
        );
    }

    @GetMapping("/notifications")
    public List<HomeNotificationDto> getNotifications(@RequestParam(defaultValue = "5") int limit) {
        List<HomeNotificationDto> list = new ArrayList<>();
        list.add(new HomeNotificationDto(1L, "INFO", "Tu plan Premium está activo", new Date(), false));
        list.add(new HomeNotificationDto(2L, "ALERT", "Pago de tarjeta vence mañana", new Date(), false));
        list.add(new HomeNotificationDto(3L, "PAYMENT", "Se registró tu último pago de S/ 200", new Date(), true));
        return list.subList(0, Math.min(limit, list.size()));
    }

    @GetMapping("/movements")
    public List<HomeMovementDto> getMovements(@RequestParam(defaultValue = "6") int limit) {
        List<HomeMovementDto> list = new ArrayList<>();
        list.add(new HomeMovementDto(new Date(), "Pago tarjeta BCP", -200.00, "PAID"));
        list.add(new HomeMovementDto(new Date(), "Ingreso salario", 2500.00, "PAID"));
        list.add(new HomeMovementDto(new Date(), "Servicio de internet", -150.00, "PENDING"));
        return list.subList(0, Math.min(limit, list.size()));
    }

    @GetMapping("/education")
    public List<EducationHighlightDto> getEducation(@RequestParam(defaultValue = "3") int limit) {
        List<EducationHighlightDto> list = new ArrayList<>();

        list.add(new EducationHighlightDto(
                1L, "Deudas", "Cómo reducir tus deudas", "BÁSICO",
                "https://finanzas.com/deudas",
                "https://www.youtube.com/embed/65seo9wDGA8"));

        list.add(new EducationHighlightDto(
                2L, "Ahorro", "Guía de ahorro mensual", "INTERMEDIO",
                "https://finanzas.com/ahorro",
                "https://www.youtube.com/embed/KdiQnd4ER5E"));

        list.add(new EducationHighlightDto(
                3L, "Inversiones", "Primeros pasos en inversiones", "AVANZADO",
                "https://finanzas.com/inversiones",
                "https://www.youtube.com/embed/mOnhcibusH8"));

        return list.subList(0, Math.min(limit, list.size()));
    }
}