package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.home.EducationHighlightDto;
import com.debtgo.debtgo_backend.service.EducationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EducationController {

    private final EducationService educationService;

    @GetMapping("/highlights")
    public List<EducationHighlightDto> listarRecursos() {
        List<EducationHighlightDto> resources = new ArrayList<>();
        resources.add(new EducationHighlightDto(1L, "Deudas", "Cómo reducir tus deudas", "Básico",
                "https://www.sbs.gob.pe/portals/3/educacion-financiera-pdf/guia_domina_tus_finanzas.pdf",
                "https://www.youtube.com/embed/65seo9wDGA8"));

        resources.add(new EducationHighlightDto(2L, "Ahorro", "Estrategias de ahorro mensual", "Intermedio",
                "https://repositorio.minedu.gob.pe/bitstream/handle/20.500.12799/10368/Educaci%C3%B3n%20econ%C3%B3mica%20y%20financiera%20documento%20de%20soporte%20para%20docentes%20de%20Educaci%C3%B3n%20Primaria%20y%20Secundaria%20Ciencias%20Sociales.pdf?isAllowed=y&sequence=1",
                "https://www.youtube.com/embed/KdiQnd4ER5E"));

        resources.add(new EducationHighlightDto(3L, "Crédito", "Mejora tu score crediticio", "Avanzado",
                "https://clasica.gref.org/nuevo/documentacion/manual_educacion_financiera.pdf",
                "https://www.youtube.com/embed/npe1KTdCGHo"));

        resources.add(new EducationHighlightDto(4L, "Inversiones", "Primeros pasos en inversiones seguras", "Avanzado",
                "https://publications.iadb.org/publications/spanish/document/Educaci%C3%B3n_financiera_Un_camino_hacla_la_inclusi%C3%B3n_Enfoques_y_experiencias_en_la_implementaci%C3%B3n_de_metodolog%C3%ADas_de_educaci%C3%B3n_financiera_para_el_ahorro_en_poblaciones_de_bajos_ingresos_es.pdf",
                "https://www.youtube.com/embed/mOnhcibusH8"));

        return resources;
    }

    @GetMapping("/{id}")
    public EducationHighlightDto obtenerPorId(@PathVariable Long id) {
        return educationService.obtenerPorId(id);
    }
}