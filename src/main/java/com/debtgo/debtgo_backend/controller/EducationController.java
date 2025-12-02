package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.EducationHighlightDto;
import com.debtgo.debtgo_backend.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EducationController {

        private final EducationService educationService;

        /**
         * ================================
         * RECURSOS DESTACADOS
         * =================================
         */
        @GetMapping("/highlights")
        public List<EducationHighlightDto> listarDestacados() {
                List<EducationHighlightDto> resources = new ArrayList<>();

                resources.add(new EducationHighlightDto(
                                1L,
                                "Deudas",
                                "Cómo reducir tus deudas",
                                "BÁSICO",
                                "https://www.sbs.gob.pe/portals/3/educacion-financiera-pdf/guia_domina_tus_finanzas.pdf",
                                "https://www.youtube.com/embed/65seo9wDGA8"));

                resources.add(new EducationHighlightDto(
                                2L,
                                "Ahorro",
                                "Estrategias para ahorrar más",
                                "INTERMEDIO",
                                "https://www.sbs.gob.pe/portals/3/educacion-financiera-pdf/guia_ahorro_efectivo.pdf",
                                "https://www.youtube.com/embed/O0P5g7TddQk"));

                resources.add(new EducationHighlightDto(
                                3L,
                                "Inversiones",
                                "Cómo invertir sin riesgo",
                                "AVANZADO",
                                "https://www.sbs.gob.pe/portals/3/educacion-financiera-pdf/manual_inversiones.pdf",
                                "https://www.youtube.com/embed/y6120QOlsfU"));

                return resources;
        }

        /**
         * ================================
         * RECURSOS DE BASE DE DATOS
         * =================================
         */
        @GetMapping("/resources")
        public List<EducationHighlightDto> listarRecursosDb() {
                return educationService.listarRecursos();
        }

        @GetMapping("/videos")
        public List<String> listarVideos() {
                return educationService.listarRecursos()
                                .stream()
                                .map(EducationHighlightDto::getVideoLink)
                                .toList();
        }
}