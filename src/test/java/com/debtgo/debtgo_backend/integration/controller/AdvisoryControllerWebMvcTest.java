package com.debtgo.debtgo_backend.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.debtgo.debtgo_backend.controller.AdvisoryController;
import com.debtgo.debtgo_backend.domain.advisory.Advisory;
import com.debtgo.debtgo_backend.dto.AdvisoryResponseDto;
import com.debtgo.debtgo_backend.service.AdvisoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(AdvisoryController.class)
public class AdvisoryControllerWebMvcTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AdvisoryService advisoryService;

    @Test
    void createAdvisory_returns200AndDto() throws Exception {
        // Arrange
        // JSON que matchea con tu AdvisoryDto (tiene title/description y .toEntity() en el controller)
        String body = """
        {
          "title": "Nueva Asesoría",
          "description": "Descripción de prueba"
        }
        """;

        Advisory saved = Advisory.builder().id(10L).title("Nueva Asesoría").description("Descripción de prueba").build();
        AdvisoryResponseDto dto = new AdvisoryResponseDto(10L, "Nueva Asesoría", "Descripción de prueba", "PENDING", null);

        Mockito.when(advisoryService.createAdvisory(eq(123L), any(Advisory.class))).thenReturn(saved);
        Mockito.when(advisoryService.toDto(saved)).thenReturn(dto);

        // Act & Assert
        mvc.perform(post("/api/advisories")
                        .param("entrepreneurId", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Nueva Asesoría"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createAdvisory_missingEntrepreneurId_returns400() throws Exception {
        // Arrange
        String body = """
        { "title": "X", "description": "Y" }
        """;

        // Act & Assert
        mvc.perform(post("/api/advisories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest()); // falta @RequestParam requerido
    }

    @Test
    void getAllAdvisories_returns200AndList() throws Exception {
        // Arrange
        Advisory a1 = Advisory.builder().id(1L).title("A1").description("D1").build();
        Advisory a2 = Advisory.builder().id(2L).title("A2").description("D2").build();

        AdvisoryResponseDto d1 = new AdvisoryResponseDto(1L, "A1", "D1", "PENDING", null);
        AdvisoryResponseDto d2 = new AdvisoryResponseDto(2L, "A2", "D2", "ACCEPTED", 77L);

        Mockito.when(advisoryService.getAll()).thenReturn(List.of(a1, a2));
        Mockito.when(advisoryService.toDto(a1)).thenReturn(d1);
        Mockito.when(advisoryService.toDto(a2)).thenReturn(d2);

        // Act & Assert
        mvc.perform(get("/api/advisories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].consultantId").value(77));
    }

    @Test
    void assignConsultant_success_returns200AndDto() throws Exception {
        // Arrange
        Advisory updated = Advisory.builder().id(9L).title("T").description("D").build();
        AdvisoryResponseDto dto = new AdvisoryResponseDto(9L, "T", "D", "ACCEPTED", 55L);

        Mockito.when(advisoryService.assignConsultant(9L, 55L)).thenReturn(Optional.of(updated));
        Mockito.when(advisoryService.toDto(updated)).thenReturn(dto);

        // Act & Assert
        mvc.perform(put("/api/advisories/{id}/assign", 9L)
                        .param("consultantId", "55"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.consultantId").value(55));
    }

    @Test
    void assignConsultant_whenServiceReturnsEmpty_returns400WithMessage() throws Exception {
        // Arrange
        Mockito.when(advisoryService.assignConsultant(111L, 222L)).thenReturn(Optional.empty());

        // Act & Assert
        mvc.perform(put("/api/advisories/{id}/assign", 111L)
                        .param("consultantId", "222"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Assignment failed")));
    }

}
