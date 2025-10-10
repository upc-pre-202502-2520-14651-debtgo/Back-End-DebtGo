package com.debtgo.debtgo_backend.integration.controller;

import com.debtgo.debtgo_backend.controller.ConsultantAppServiceController;
import com.debtgo.debtgo_backend.dto.ConsultantServiceDto;
import com.debtgo.debtgo_backend.service.ConsultantAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultantAppServiceController.class)
public class ConsultantAppServiceControllerWebMvcTest {


    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ConsultantAppService service;

    @Test
    void list_byConsultant_returns200AndJsonArray() throws Exception {
        // Arrange
        var dto1 = ConsultantServiceDto.builder()
                .id(1L).title("Plan Básico").description("Desc 1").price(50.0).consultantId(10L).build();
        var dto2 = ConsultantServiceDto.builder()
                .id(2L).title("Plan Pro").description("Desc 2").price(120.0).consultantId(10L).build();
        Mockito.when(service.byConsultant(10L)).thenReturn(List.of(dto1, dto2));

        // Act & Assert
        mvc.perform(get("/api/v1/services/by-consultant/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Plan Básico"))
                .andExpect(jsonPath("$[1].price").value(120.0));
    }

    @Test
    void create_returns200AndCreatedDto() throws Exception {
        // Arrange
        var input = ConsultantServiceDto.builder()
                .title("Nuevo").description("Desc").price(75.0).consultantId(10L).build();
        var saved = ConsultantServiceDto.builder()
                .id(99L).title("Nuevo").description("Desc").price(75.0).consultantId(10L).build();
        Mockito.when(service.createService(any(ConsultantServiceDto.class))).thenReturn(saved);

        // Act & Assert
        mvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.title").value("Nuevo"))
                .andExpect(jsonPath("$.consultantId").value(10));
    }

    @Test
    void update_returns200AndUpdatedDto() throws Exception {
        // Arrange
        var update = ConsultantServiceDto.builder()
                .title("Editado").description("Nueva desc").price(200.0).build();
        var updated = ConsultantServiceDto.builder()
                .id(7L).title("Editado").description("Nueva desc").price(200.0).consultantId(10L).build();
        Mockito.when(service.updateService(eq(7L), any(ConsultantServiceDto.class))).thenReturn(updated);

        // Act & Assert
        mvc.perform(put("/api/v1/services/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Editado"))
                .andExpect(jsonPath("$.price").value(200.0));
    }

    @Test
    void delete_returns200AndNoContentBody() throws Exception {
        // Arrange
        // (no hace falta stub: método void)
        // Act & Assert
        mvc.perform(delete("/api/v1/services/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // controlador retorna void → cuerpo vacío
        Mockito.verify(service).deleteService(5L);
    }

}
