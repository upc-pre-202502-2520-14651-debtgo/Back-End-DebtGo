package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.service.ConsultantAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultants")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ConsultantServicesRestController {

    private final ConsultantAppService app;

    @GetMapping("/{id}/services")
    public List<ConsultantServiceDto> list(@PathVariable Long id) {
        return app.byConsultant(id);
    }

    @PostMapping("/services")
    public ConsultantServiceDto create(@RequestBody ConsultantServiceDto dto) {
        return app.createService(dto);
    }

    @PutMapping("/services/{id}")
    public ConsultantServiceDto update(
            @PathVariable Long id,
            @RequestBody ConsultantServiceDto dto) {
        return app.updateService(id, dto);
    }

    @DeleteMapping("/services/{id}")
    public void delete(@PathVariable Long id) {
        app.deleteService(id);
    }
}