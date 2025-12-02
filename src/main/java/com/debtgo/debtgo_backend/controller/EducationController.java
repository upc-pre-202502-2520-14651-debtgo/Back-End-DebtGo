package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.home.EducationHighlightDto;
import com.debtgo.debtgo_backend.service.EducationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EducationController {

        private final EducationService educationService;

        @GetMapping("/highlights")
        public List<EducationHighlightDto> listarRecursos() {
                return educationService.listarRecursos();
        }

        @GetMapping("/{id}")
        public EducationHighlightDto obtenerPorId(@PathVariable Long id) {
                return educationService.obtenerPorId(id);
        }
}