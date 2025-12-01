package com.debtgo.debtgo_backend.integration.controller;

import com.debtgo.debtgo_backend.controller.ConsultantProfileController;
import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.dto.ConsultantProfileDto;
import com.debtgo.debtgo_backend.service.ConsultantProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultantProfileController.class)
public class ConsultantProfileControllerWebMvcTest {
        @Autowired
        MockMvc mvc;

        @Autowired
        ObjectMapper mapper;

        ConsultantProfileService consultantService;

        @Test
        void createProfile_returns200AndSavedEntity() throws Exception {
                // Arrange
                ConsultantProfileDto input = new ConsultantProfileDto(null, "Ana", "Finanzas", 60.0);

                User u = new User("ana@mail.com", "pwd", "CONSULTANT");
                u.setId(10L);

                ConsultantProfile saved = ConsultantProfile.builder()
                                .id(99L)
                                .name("Ana")
                                .skills("Finanzas")
                                .rate(60.0)
                                .user(u)
                                .build();

                Mockito.when(consultantService.createProfile(eq("ana@mail.com"), any(ConsultantProfile.class)))
                                .thenReturn(saved);

                // Act & Assert
                mvc.perform(post("/api/consultants/profile/{email}", "ana@mail.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(input)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // Assert (campos básicos del Profile devuelto)
                                .andExpect(jsonPath("$.id").value(99))
                                .andExpect(jsonPath("$.name").value("Ana"))
                                .andExpect(jsonPath("$.skills").value("Finanzas"))
                                .andExpect(jsonPath("$.rate").value(60.0));
        }

        @Test
        void getProfileByUser_whenFound_returns200AndProfile() throws Exception {
                // Arrange
                ConsultantProfile profile = ConsultantProfile.builder()
                                .id(5L)
                                .name("Carlos")
                                .skills("Legal")
                                .rate(80.0)
                                .build();

                Mockito.when(consultantService.getProfileByUser("carlos@mail.com"))
                                .thenReturn(Optional.of(profile));

                // Act & Assert
                mvc.perform(get("/api/consultants/profile/{email}", "carlos@mail.com"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // Assert
                                .andExpect(jsonPath("$.id").value(5))
                                .andExpect(jsonPath("$.name").value("Carlos"))
                                .andExpect(jsonPath("$.skills").value("Legal"))
                                .andExpect(jsonPath("$.rate").value(80.0));
        }

        @Test
        void getProfileByUser_whenNotFound_returns404() throws Exception {
                // Arrange
                Mockito.when(consultantService.getProfileByUser("nope@mail.com"))
                                .thenReturn(Optional.empty());

                // Act & Assert
                mvc.perform(get("/api/consultants/profile/{email}", "nope@mail.com"))
                                .andExpect(status().isNotFound());
        }
}