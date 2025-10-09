package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.Consultant;
import com.debtgo.debtgo_backend.domain.ConsultantRequest;
import com.debtgo.debtgo_backend.domain.ConsultantServiceEntity;
import com.debtgo.debtgo_backend.dto.ConsultantDto;
import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.dto.ConsultantSummaryDto;
import com.debtgo.debtgo_backend.repository.ConsultantRepository;
import com.debtgo.debtgo_backend.repository.ConsultantRequestRepository;
import com.debtgo.debtgo_backend.repository.ConsultantServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultantAppServiceTest {
    @Mock
    private ConsultantRepository consultantRepo;

    @Mock
    private ConsultantServiceRepository serviceRepo;

    @Mock
    private ConsultantRequestRepository requestRepo;

    @InjectMocks
    private ConsultantAppService sut; // system under test

    private Consultant sampleConsultant;
    private ConsultantServiceEntity sampleService;
    private ConsultantRequest requestInProgress;
    private ConsultantRequest requestCompleted;

    @BeforeEach
    void setUp() {
        sampleConsultant = new Consultant();
        sampleConsultant.setId(100L);
        sampleConsultant.setFullName("Test Consultant");
        sampleConsultant.setSpecialty("Finanzas");
        sampleConsultant.setExperience("5");
        sampleConsultant.setDescription("Descripcion");
        sampleConsultant.setHourlyRate(50.0);
        sampleConsultant.setProfileImage("img.png");
        sampleConsultant.setRating(4.8);

        sampleService = ConsultantServiceEntity.builder()
                .id(200L)
                .title("Servicio A")
                .description("Desc A")
                .price(120.0)
                .consultant(sampleConsultant)
                .build();

        requestInProgress = new ConsultantRequest();
        requestInProgress.setId(300L);
        requestInProgress.setStatus("IN_PROGRESS");

        requestCompleted = new ConsultantRequest();
        requestCompleted.setId(301L);
        requestCompleted.setStatus("COMPLETED");
    }

    // ---------- PERFIL ----------
    @Test
    void getConsultant_whenExists_returnsDto() {
        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));

        ConsultantDto dto = sut.getConsultant(100L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Test Consultant", dto.getFullName());
        assertEquals("Finanzas", dto.getSpecialty());
        verify(consultantRepo).findById(100L);
    }

    @Test
    void getConsultant_whenNotFound_throwsRuntimeException() {
        when(consultantRepo.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.getConsultant(999L));
        assertTrue(ex.getMessage().toLowerCase().contains("consultor"));
        verify(consultantRepo).findById(999L);
    }

    @Test
    void updateConsultant_updatesFieldsAndSaves() {
        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));
        when(consultantRepo.save(any(Consultant.class))).thenAnswer(inv -> inv.getArgument(0));

        ConsultantDto input = ConsultantDto.builder()
                .fullName("Nuevo Nombre")
                .specialty("Nuevo")
                .experience("10")
                .description("Nueva desc")
                .hourlyRate(80.0)
                .profileImage("new.png")
                .rating(4.9)
                .build();

        ConsultantDto result = sut.updateConsultant(100L, input);

        assertNotNull(result);
        assertEquals("Nuevo Nombre", result.getFullName());
        assertEquals("10", result.getExperience());
        verify(consultantRepo).findById(100L);
        verify(consultantRepo).save(any(Consultant.class));
    }

    // ---------- RESUMEN ----------
    @Test
    void getSummary_computesSummaryCorrectly() {
        // services: 1
        when(serviceRepo.findByConsultantId(100L)).thenReturn(List.of(sampleService));
        // cases: 2 (one in progress, one completed)
        when(requestRepo.findByConsultantId(100L)).thenReturn(List.of(requestInProgress, requestCompleted));
        // rating from consultant
        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));

        ConsultantSummaryDto summary = sut.getSummary(100L);

        assertNotNull(summary);
        assertEquals(1L, summary.getPublishedServices());
        assertEquals(1L, summary.getActiveAdvisories());
        assertEquals(1L, summary.getServedClients());
        assertEquals(4.8, summary.getAvgRating());
        verify(serviceRepo).findByConsultantId(100L);
        verify(requestRepo).findByConsultantId(100L);
        verify(consultantRepo).findById(100L);
    }

    // ---------- SERVICIOS ----------
    @Test
    void byConsultant_mapsServicesToDtoList() {
        when(serviceRepo.findByConsultantId(100L)).thenReturn(List.of(sampleService));

        List<ConsultantServiceDto> list = sut.byConsultant(100L);

        assertNotNull(list);
        assertEquals(1, list.size());
        ConsultantServiceDto dto = list.get(0);
        assertEquals(200L, dto.getId());
        assertEquals("Servicio A", dto.getTitle());
        assertEquals(100L, dto.getConsultantId());
        verify(serviceRepo).findByConsultantId(100L);
    }

    @Test
    void createService_whenConsultantExists_createsAndReturnsDto() {
        ConsultantServiceDto input = ConsultantServiceDto.builder()
                .title("Nuevo Servicio")
                .description("Desc")
                .price(75.0)
                .consultantId(100L)
                .build();

        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));
        when(serviceRepo.save(any(ConsultantServiceEntity.class))).thenAnswer(inv -> {
            ConsultantServiceEntity s = inv.getArgument(0);
            s.setId(555L);
            return s;
        });

        ConsultantServiceDto result = sut.createService(input);

        assertNotNull(result);
        assertEquals(555L, result.getId());
        assertEquals("Nuevo Servicio", result.getTitle());
        assertEquals(100L, result.getConsultantId());
        verify(consultantRepo).findById(100L);
        verify(serviceRepo).save(any(ConsultantServiceEntity.class));
    }

    @Test
    void createService_whenConsultantNotFound_throwsRuntimeException() {
        ConsultantServiceDto input = ConsultantServiceDto.builder()
                .title("X").description("Y").price(1.0).consultantId(999L).build();

        when(consultantRepo.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.createService(input));
        assertTrue(ex.getMessage().toLowerCase().contains("consultor"));
        verify(consultantRepo).findById(999L);
        verify(serviceRepo, never()).save(any());
    }

    @Test
    void updateService_whenExists_updatesAndReturnsDto() {
        ConsultantServiceEntity existing = ConsultantServiceEntity.builder()
                .id(777L)
                .title("Old")
                .description("Old")
                .price(10.0)
                .consultant(sampleConsultant)
                .build();

        ConsultantServiceDto updateDto = ConsultantServiceDto.builder()
                .title("Updated")
                .description("New Desc")
                .price(20.0)
                .build();

        when(serviceRepo.findById(777L)).thenReturn(Optional.of(existing));
        when(serviceRepo.save(any(ConsultantServiceEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ConsultantServiceDto res = sut.updateService(777L, updateDto);

        assertNotNull(res);
        assertEquals("Updated", res.getTitle());
        assertEquals(20.0, res.getPrice());
        verify(serviceRepo).findById(777L);
        verify(serviceRepo).save(existing);
    }

    @Test
    void updateService_whenNotFound_throwsRuntimeException() {
        when(serviceRepo.findById(888L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.updateService(888L, ConsultantServiceDto.builder().build()));
        assertTrue(ex.getMessage().toLowerCase().contains("servicio"));
        verify(serviceRepo).findById(888L);
    }

    @Test
    void deleteService_callsRepositoryDelete() {
        doNothing().when(serviceRepo).deleteById(123L);

        sut.deleteService(123L);

        verify(serviceRepo).deleteById(123L);
    }

    // ---------- CASOS ----------
    @Test
    void cases_returnsListFromRepo() {
        when(requestRepo.findByConsultantId(100L)).thenReturn(List.of(requestInProgress, requestCompleted));

        List<ConsultantRequest> list = sut.cases(100L);

        assertEquals(2, list.size());
        verify(requestRepo).findByConsultantId(100L);
    }

    @Test
    void updateCaseStatus_whenExists_updatesAndSaves() {
        ConsultantRequest r = new ConsultantRequest();
        r.setId(400L);
        r.setStatus("NEW");

        when(requestRepo.findById(400L)).thenReturn(Optional.of(r));
        when(requestRepo.save(any(ConsultantRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        sut.updateCaseStatus(400L, "COMPLETED");

        assertEquals("COMPLETED", r.getStatus());
        verify(requestRepo).findById(400L);
        verify(requestRepo).save(r);
    }

    @Test
    void updateCaseStatus_whenNotFound_throwsRuntimeException() {
        when(requestRepo.findById(9999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.updateCaseStatus(9999L, "X"));
        assertTrue(ex.getMessage().toLowerCase().contains("caso"));
        verify(requestRepo).findById(9999L);
        verify(requestRepo, never()).save(any());
    }

    // ---------- MÉTRICAS ----------
    @Test
    void metrics_computesCorrectValues() {
        ConsultantRequest r1 = new ConsultantRequest(); r1.setStatus("COMPLETED");
        ConsultantRequest r2 = new ConsultantRequest(); r2.setStatus("IN_PROGRESS");
        when(requestRepo.findByConsultantId(100L)).thenReturn(List.of(r1, r2));
        ConsultantServiceEntity s1 = ConsultantServiceEntity.builder().price(100.0).consultant(sampleConsultant).build();
        ConsultantServiceEntity s2 = ConsultantServiceEntity.builder().price(50.0).consultant(sampleConsultant).build();
        when(serviceRepo.findByConsultantId(100L)).thenReturn(List.of(s1, s2));
        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));

        Map<String, Object> metrics = sut.metrics(100L);

        assertEquals(1L, ((Number) metrics.get("sessions")).longValue());
        assertEquals(150.0, ((Number) metrics.get("revenue")).doubleValue());
        assertEquals(4.8, ((Number) metrics.get("avgRating")).doubleValue());
        assertTrue(metrics.containsKey("compliance"));
        verify(requestRepo).findByConsultantId(100L);
        verify(serviceRepo).findByConsultantId(100L);
        verify(consultantRepo).findById(100L);
    }

    // ---------- CREAR / BUSCAR CONSULTANT ----------
    @Test
    void createConsultant_savesAndReturnsEntity() {
        Consultant toCreate = new Consultant();
        toCreate.setFullName("Nuevo");
        when(consultantRepo.save(any(Consultant.class))).thenAnswer(inv -> {
            Consultant c = inv.getArgument(0);
            c.setId(999L);
            return c;
        });

        Consultant saved = sut.createConsultant(toCreate);

        assertNotNull(saved);
        assertEquals(999L, saved.getId());
        verify(consultantRepo).save(toCreate);
    }

    @Test
    void findConsultantById_whenExists_returnsEntity() {
        when(consultantRepo.findById(100L)).thenReturn(Optional.of(sampleConsultant));

        Consultant c = sut.findConsultantById(100L);

        assertNotNull(c);
        assertEquals(100L, c.getId());
        verify(consultantRepo).findById(100L);
    }

    @Test
    void findConsultantById_whenNotFound_throwsRuntimeException() {
        when(consultantRepo.findById(5555L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.findConsultantById(5555L));
        assertTrue(ex.getMessage().toLowerCase().contains("consultant"));
        verify(consultantRepo).findById(5555L);
    }
}
