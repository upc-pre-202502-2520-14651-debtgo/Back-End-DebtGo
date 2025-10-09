package com.debtgo.debtgo_backend.service;
import com.debtgo.debtgo_backend.domain.debt.Debt;
import com.debtgo.debtgo_backend.dto.debt.DebtSummaryDto;
import com.debtgo.debtgo_backend.repository.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class DebtServiceTest {


    @Mock
    private DebtRepository debtRepository;

    @InjectMocks
    private DebtService debtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSummary_returnsCorrectMetrics() {
        // Arrange
        when(debtRepository.countActiveClients()).thenReturn(10L);
        when(debtRepository.countByStatus("LATE")).thenReturn(3L);
        when(debtRepository.countByStatus("ACTIVE")).thenReturn(7L);
        when(debtRepository.calculateComplianceRate()).thenReturn(85.5);

        // Act
        Map<String, Object> summary = debtService.getSummary();

        // Assert
        assertEquals(10L, summary.get("activeClients"));
        assertEquals(3L, summary.get("overdueDebts"));
        assertEquals(7L, summary.get("activePlans"));
        assertEquals(85.5, summary.get("complianceRate"));
        verify(debtRepository, times(1)).countActiveClients();
    }

    @Test
    void getAllDebts_returnsMappedDebtSummaryList() {
        // Arrange
        Debt d1 = Debt.builder()
                .id(1L)
                .clientName("Carlos Pérez")
                .totalDebt(5000.0)
                .monthlyPayment(500.0)
                .dueDate(LocalDate.of(2025, 11, 15))
                .status("ACTIVE")
                .build();

        when(debtRepository.findAll()).thenReturn(List.of(d1));

        // Act
        var result = debtService.getAllDebts();

        // Assert
        assertEquals(1, result.size());
        DebtSummaryDto dto = result.get(0);
        assertEquals("Carlos Pérez", dto.getClientName());
        assertEquals(5000.0, dto.getTotalDebt());
        assertEquals("ACTIVE", dto.getStatus());
        verify(debtRepository, times(1)).findAll();
    }

    @Test
    void createDebt_savesDebtWithProvidedValues() {
        // Arrange
        DebtSummaryDto dto = DebtSummaryDto.builder()
                .clientName("Ana Gómez")
                .totalDebt(8000.0)
                .monthlyPayment(800.0)
                .dueDate(LocalDate.of(2025, 12, 1))
                .status("LATE")
                .build();

        Debt savedDebt = Debt.builder()
                .id(1L)
                .clientName(dto.getClientName())
                .totalDebt(dto.getTotalDebt())
                .monthlyPayment(dto.getMonthlyPayment())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();

        when(debtRepository.save(any(Debt.class))).thenReturn(savedDebt);

        // Act
        Debt result = debtService.createDebt(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Ana Gómez", result.getClientName());
        assertEquals("LATE", result.getStatus());
        verify(debtRepository, times(1)).save(any(Debt.class));
    }

    @Test
    void createDebt_setsDefaultValuesWhenMissing() {
        // Arrange
        DebtSummaryDto dto = DebtSummaryDto.builder()
                .clientName("Pedro Ruiz")
                .totalDebt(6000.0)
                .monthlyPayment(600.0)
                .build(); // sin fecha ni estado

        Debt expected = Debt.builder()
                .id(1L)
                .clientName("Pedro Ruiz")
                .totalDebt(6000.0)
                .monthlyPayment(600.0)
                .dueDate(LocalDate.now().plusMonths(1))
                .status("ACTIVE")
                .build();

        when(debtRepository.save(any(Debt.class))).thenReturn(expected);

        // Act
        Debt result = debtService.createDebt(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Pedro Ruiz", result.getClientName());
        assertEquals("ACTIVE", result.getStatus());
        assertNotNull(result.getDueDate());
        verify(debtRepository, times(1)).save(any(Debt.class));
    }
}
