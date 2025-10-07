package com.debtgo.debtgo_backend.controller;

import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.service.ConsultantAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultantAppServiceController {

  private final ConsultantAppService service;

  @GetMapping("/by-consultant/{id}")
  public List<ConsultantServiceDto> list(@PathVariable Long id) {
    return service.byConsultant(id);
  }

  @PostMapping
  public ConsultantServiceDto create(@RequestBody ConsultantServiceDto dto) {
    return service.createService(dto);
  }

  @PutMapping("/{id}")
  public ConsultantServiceDto update(@PathVariable Long id, @RequestBody ConsultantServiceDto dto) {
    return service.updateService(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.deleteService(id);
  }
}