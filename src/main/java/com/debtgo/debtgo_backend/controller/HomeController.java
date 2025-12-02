package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.EducationHighlightDto;
import com.debtgo.debtgo_backend.dto.home.*;
import com.debtgo.debtgo_backend.service.EducationService;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HomeController {

    private final EducationService educationService;

    @GetMapping("/summary")
    public HomeSummaryDto getSummary() {
        return new HomeSummaryDto(3, 4500.75, 2, 1);
    }

    @GetMapping("/notifications")
    public List<HomeNotificationDto> getNotifications(@RequestParam(defaultValue = "5") int limit) {
        List<HomeNotificationDto> list = new ArrayList<>();
        list.add(new HomeNotificationDto(1L, "INFO", "Tu plan Premium está activo", new Date(), false));
        list.add(new HomeNotificationDto(2L, "ALERT", "Pago de tarjeta vence mañana", new Date(), false));
        list.add(new HomeNotificationDto(3L, "PAYMENT", "Se registró un pago de S/ 200", new Date(), true));
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
    public List<HomeEducationDto> getEducation(@RequestParam(defaultValue = "3") int limit) {

        List<EducationHighlightDto> recursos = educationService.listarRecursos();

        return recursos.stream()
                .limit(limit)
                .map(r -> new HomeEducationDto(
                        r.getId(),
                        r.getTitle(),
                        r.getCategory(),
                        r.getLevel(),
                        r.getPdfLink(),
                        r.getVideoLink()))
                .toList();
    }
}