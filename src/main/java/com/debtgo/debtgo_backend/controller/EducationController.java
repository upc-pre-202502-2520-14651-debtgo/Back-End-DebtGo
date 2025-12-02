package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.home.EducationHighlightDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/education")
@CrossOrigin(origins = "*")
public class EducationController {

        @GetMapping("/highlights")
        public List<EducationHighlightDto> getHighlights(@RequestParam(defaultValue = "3") int limit) {

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