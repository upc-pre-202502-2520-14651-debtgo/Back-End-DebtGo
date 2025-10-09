package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.domain.advisory.AdvisoryStatus;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.dto.AdvisoryResponseDto;
import com.debtgo.debtgo_backend.repository.AdvisoryRepository;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AdvisoryServiceTest {
    @Mock
    private AdvisoryRepository advisoryRepo;

    @Mock
    private ConsultantProfileRepository consultantRepo;

    @InjectMocks
    private AdvisoryService advisoryService;

    private Advisory sampleAdvisory;
    private ConsultantProfile sampleConsultant;

    @BeforeEach
    void setUp() {
        sampleAdvisory = new Advisory();
        sampleAdvisory.setId(10L);
        sampleAdvisory.setTitle("Asesoría prueba");
        sampleAdvisory.setDescription("Descripción prueba");

        sampleConsultant = new ConsultantProfile();
        sampleConsultant.setId(5L);
        sampleConsultant.setName("Consultor Test");
    }

    @Test
    void createAdvisory_whenConsultantIdIsNull_setsPendingAndSaves() {
        // Arrange
        Advisory toSave = new Advisory();
        toSave.setTitle("T");
        toSave.setDescription("D");

        when(advisoryRepo.save(any(Advisory.class))).thenAnswer(invocation -> {
            Advisory a = invocation.getArgument(0);
            a.setId(1L);
            return a;
        });

        // Act
        Advisory saved = advisoryService.createAdvisory(null, toSave);

        // Assert
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals(AdvisoryStatus.PENDING, saved.getStatus());
        verify(advisoryRepo, times(1)).save(toSave);
    }

    @Test
    void createAdvisory_withConsultantId_assignsConsultantAndSaves() {
        // Arrange
        Advisory toSave = new Advisory();
        toSave.setTitle("Con consultor");
        toSave.setDescription("Desc");

        when(consultantRepo.findById(5L)).thenReturn(Optional.of(sampleConsultant));
        when(advisoryRepo.save(any(Advisory.class))).thenAnswer(invocation -> {
            Advisory a = invocation.getArgument(0);
            a.setId(2L);
            return a;
        });

        // Act
        Advisory result = advisoryService.createAdvisory(5L, toSave);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(AdvisoryStatus.PENDING, result.getStatus());
        assertNotNull(result.getConsultant());
        assertEquals(5L, result.getConsultant().getId());
        verify(consultantRepo).findById(5L);
        verify(advisoryRepo).save(toSave);
    }

    @Test
    void createAdvisory_whenConsultantNotFound_throwsRuntimeException() {
        // Arrange
        Advisory toSave = new Advisory();
        when(consultantRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                advisoryService.createAdvisory(99L, toSave));
        assertTrue(ex.getMessage().toLowerCase().contains("consultant"));
        verify(consultantRepo).findById(99L);
        verify(advisoryRepo, never()).save(any());
    }

    @Test
    void getAll_returnsAllAdvisories() {
        // Arrange
        Advisory a1 = new Advisory();
        a1.setId(1L);
        Advisory a2 = new Advisory();
        a2.setId(2L);
        when(advisoryRepo.findAll()).thenReturn(Arrays.asList(a1, a2));

        // Act
        List<Advisory> list = advisoryService.getAll();

        // Assert
        assertNotNull(list);
        assertEquals(2, list.size());
        verify(advisoryRepo).findAll();
    }

    @Test
    void assignConsultant_success_assignsAndSetsAccepted() {
        // Arrange
        Advisory existing = new Advisory();
        existing.setId(20L);
        existing.setStatus(AdvisoryStatus.PENDING);

        when(advisoryRepo.findById(20L)).thenReturn(Optional.of(existing));
        when(consultantRepo.findById(5L)).thenReturn(Optional.of(sampleConsultant));
        when(advisoryRepo.save(any(Advisory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Advisory> opt = advisoryService.assignConsultant(20L, 5L);

        // Assert
        assertTrue(opt.isPresent());
        Advisory updated = opt.get();
        assertEquals(AdvisoryStatus.ACCEPTED, updated.getStatus());
        assertNotNull(updated.getConsultant());
        assertEquals(5L, updated.getConsultant().getId());
        verify(advisoryRepo).findById(20L);
        verify(consultantRepo).findById(5L);
        verify(advisoryRepo).save(existing);
    }

    @Test
    void assignConsultant_whenAdvisoryNotFound_throwsRuntimeException() {
        // Arrange
        when(advisoryRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                advisoryService.assignConsultant(999L, 5L));
        assertTrue(ex.getMessage().toLowerCase().contains("advisory"));
        verify(advisoryRepo).findById(999L);
        verify(consultantRepo, never()).findById(any());
        verify(advisoryRepo, never()).save(any());
    }

    @Test
    void toDto_mapsAdvisoryToResponseDto_correctly() {
        // Arrange
        Advisory adv = new Advisory();
        adv.setId(55L);
        adv.setTitle("Titulo");
        adv.setDescription("Desc");
        adv.setStatus(AdvisoryStatus.ACCEPTED);
        ConsultantProfile c = new ConsultantProfile();
        c.setId(7L);
        adv.setConsultant(c);

        // Act
        AdvisoryResponseDto dto = advisoryService.toDto(adv);

        // Assert
        assertNotNull(dto);
        assertEquals(55L, dto.getId());
        assertEquals("Titulo", dto.getTitle());
        assertEquals("Desc", dto.getDescription());
        assertEquals(AdvisoryStatus.ACCEPTED.name(), dto.getStatus());
        assertEquals(7L, dto.getConsultantId());
    }
}
