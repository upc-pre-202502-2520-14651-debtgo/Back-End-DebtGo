package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.dto.MetricsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {

    public MetricsDto getMetrics(Long consultantId) {

        // LÓGICA MOCK (puedes conectarlo a la BD luego)
        return new MetricsDto(
                12, // total cases
                9, // resolved
                4.7 // satisfaction
        );
    }
}