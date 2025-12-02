package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.home.EducationHighlightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EducationController {

        // Lista estática simulando BD
        private static final List<EducationHighlightDto> RESOURCES = List.of(
                        new EducationHighlightDto(
                                        1L,
                                        "Deudas",
                                        "Cómo reducir tus deudas",
                                        "Básico",
                                        "https://www.sbs.gob.pe/portals/3/educacion-financiera-pdf/guia_domina_tus_finanzas.pdf",
                                        "https://www.youtube.com/embed/65seo9wDGA8"),
                        new EducationHighlightDto(
                                        2L,
                                        "Ahorro",
                                        "Estrategias de ahorro mensual",
                                        "Intermedio",
                                        "https://repositorio.minedu.gob.pe/bitstream/handle/20.500.12799/10368/EducacionFinanciera.pdf?sequence=1",
                                        "https://www.youtube.com/embed/KdiQnd4ER5E"),
                        new EducationHighlightDto(
                                        3L,
                                        "Crédito",
                                        "Mejora tu score crediticio",
                                        "Avanzado",
                                        "https://clasica.gref.org/nuevo/documentacion/manual_educacion_financiera.pdf",
                                        "https://www.youtube.com/embed/npe1KTdCGHo"),
                        new EducationHighlightDto(
                                        4L,
                                        "Inversiones",
                                        "Primeros pasos en inversiones seguras",
                                        "Avanzado",
                                        "https://publications.iadb.org/publications/spanish/document/EducacionFinancieraInclusion.pdf",
                                        "https://www.youtube.com/embed/mOnhcibusH8"));

        @GetMapping("/highlights")
        public List<EducationHighlightDto> listarRecursos() {
                return RESOURCES;
        }

        @GetMapping("/{id}")
        public EducationHighlightDto obtenerPorId(@PathVariable Long id) {
                return RESOURCES.stream()
                                .filter(r -> r.getId().equals(id))
                                .findFirst()
                                .orElse(null);
        }
}