package com.debtgo.debtgo_backend.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("/api/v1/relations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RelationsController {

    @GetMapping("/history/{id}")
    public List<Map<String, Object>> getConsultantHistory(@PathVariable Long id) {

        List<Map<String, Object>> history = new ArrayList<>();

        // Ejemplo para mostrar datos en el frontend
        Map<String, Object> sample = new HashMap<>();
        sample.put("caseId", 1);
        sample.put("title", "Asesoría financiera básica");
        sample.put("status", "COMPLETED");
        sample.put("date", new Date());

        history.add(sample);

        return history;
    }
}