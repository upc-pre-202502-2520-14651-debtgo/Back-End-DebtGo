package com.debtgo.debtgo_backend.service;


import com.debtgo.debtgo_backend.domain.simulation.Simulation;
import com.debtgo.debtgo_backend.dto.simulation.SimulationRequestDto;
import com.debtgo.debtgo_backend.dto.simulation.SimulationResponseDto;
import com.debtgo.debtgo_backend.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationServiceTest {
    @Mock
    private SimulationRepository repo;

    @InjectMocks
    private SimulationService sut;

    private SimulationRequestDto baseDto;

    @BeforeEach
    void setUp() {
        baseDto = new SimulationRequestDto();
        baseDto.setAmount(1000.0);
        baseDto.setAnnualRate(12.0);
        baseDto.setMonths(12);
        baseDto.setExtra(0.0);
        baseDto.setStartDate(LocalDate.of(2025, 1, 1));
        baseDto.setUserId(42L);
    }

    @Test
    void preview_returnsCalculatedSchedule_andMetrics() {
        // Arrange
        SimulationRequestDto dto = baseDto;

        // Act
        SimulationResponseDto resp = sut.preview(dto);

        // Assert
        assertNotNull(resp);
        assertTrue(resp.getMonthlyPayment() > 0, "Debe calcular una cuota mensual positiva");
        assertTrue(resp.getTotalPayment() >= dto.getAmount(), "El pago total no puede ser menor al monto inicial");
        assertNotNull(resp.getSchedule());
        assertFalse(resp.getSchedule().isEmpty(), "El cronograma no debe estar vacío");
    }

    @Test
    void create_savesSimulationAndReturnsResponse_withSimulationIdSet() {
        // Arrange
        SimulationRequestDto dto = baseDto;
        Simulation saved = Simulation.builder()
                .id(55L)
                .amount(dto.getAmount())
                .annualRate(dto.getAnnualRate())
                .months(dto.getMonths())
                .extra(dto.getExtra())
                .startDate(dto.getStartDate())
                .monthlyPayment(85.0)
                .totalInterest(20.0)
                .totalPayment(1020.0)
                .userId(dto.getUserId())
                .build();

        when(repo.save(any(Simulation.class))).thenReturn(saved);

        // Act
        SimulationResponseDto result = sut.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals(55L, result.getSimulationId());
        verify(repo).save(any(Simulation.class));

        ArgumentCaptor<Simulation> captor = ArgumentCaptor.forClass(Simulation.class);
        verify(repo).save(captor.capture());
        Simulation toSave = captor.getValue();
        assertEquals(dto.getAmount(), toSave.getAmount());
        assertEquals(dto.getAnnualRate(), toSave.getAnnualRate());
        assertEquals(dto.getMonths(), toSave.getMonths());
        assertEquals(dto.getUserId(), toSave.getUserId());
    }

    @Test
    void calculate_zeroInterest_usesPrincipalDividedByMonths_andScheduleLengthEqualsMonths() {
        // Arrange
        SimulationRequestDto dto = new SimulationRequestDto();
        dto.setAmount(1200.0);
        dto.setAnnualRate(0.0);
        dto.setMonths(12);
        dto.setExtra(0.0);
        dto.setStartDate(LocalDate.of(2025, 5, 1));
        dto.setUserId(7L);

        // Act
        SimulationResponseDto resp = sut.preview(dto);

        // Assert
        // cuota = P / n = 1200 / 12 = 100.0 (redondeado)
        assertEquals(100.0, resp.getMonthlyPayment(), 0.001);
        // en interés 0, el pago total debería aproximarse al principal
        assertEquals(1200.0, resp.getTotalPayment(), 0.01);
        // esperamos que el cronograma tenga exactamente 'months' entradas
        assertEquals(12, resp.getSchedule().size());
    }

    @Test
    void listByUser_returnsRepoResults() {
        // Arrange
        Simulation s1 = Simulation.builder().id(1L).userId(100L).build();
        Simulation s2 = Simulation.builder().id(2L).userId(100L).build();
        when(repo.findByUserId(100L)).thenReturn(List.of(s1, s2));

        // Act
        List<Simulation> list = sut.listByUser(100L);

        // Assert
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        verify(repo).findByUserId(100L);
    }
}
