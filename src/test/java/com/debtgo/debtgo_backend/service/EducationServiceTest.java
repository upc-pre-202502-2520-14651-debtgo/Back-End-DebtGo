package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.education.EducationHighlight;
import com.debtgo.debtgo_backend.dto.home.EducationHighlightDto;
import com.debtgo.debtgo_backend.repository.EducationHighlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EducationServiceTest {


    @Mock
    private EducationHighlightRepository educationRepo;

    @InjectMocks
    private EducationService sut;

    private EducationHighlight e1;
    private EducationHighlight e2;

    @BeforeEach
    void setUp() {
        e1 = EducationHighlight.builder()
                .id(1L)
                .title("Ahorro Básico")
                .category("Finanzas")
                .level("Beginner")
                .link("http://link1")
                .pdfLink("http://pdf1")
                .videoLink("http://video1")
                .build();

        e2 = EducationHighlight.builder()
                .id(2L)
                .title("Crédito Inteligente")
                .category("Crédito")
                .level("Intermediate")
                .link("http://link2")
                .pdfLink("http://pdf2")
                .videoLink("http://video2")
                .build();
    }

    @Test
    void listarRecursos_mapsAllEntitiesToDtos() {
        // Arrange
        when(educationRepo.findAll()).thenReturn(List.of(e1, e2));

        // Act
        List<EducationHighlightDto> result = sut.listarRecursos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        EducationHighlightDto dto1 = result.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Ahorro Básico", dto1.getTitle());
        assertEquals("Finanzas", dto1.getCategory());
        assertEquals("Beginner", dto1.getLevel());
        assertEquals("http://pdf1", dto1.getPdfLink());
        assertEquals("http://video1", dto1.getVideoLink());
        verify(educationRepo).findAll();
    }

    @Test
    void obtenerPorId_whenExists_returnsDto() {
        // Arrange
        when(educationRepo.findById(2L)).thenReturn(Optional.of(e2));

        // Act
        EducationHighlightDto dto = sut.obtenerPorId(2L);

        // Assert
        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Crédito Inteligente", dto.getTitle());
        assertEquals("Crédito", dto.getCategory());
        assertEquals("Intermediate", dto.getLevel());
        assertEquals("http://pdf2", dto.getPdfLink());
        assertEquals("http://video2", dto.getVideoLink());
        verify(educationRepo).findById(2L);
    }

    @Test
    void obtenerPorId_whenNotFound_throwsRuntimeException() {
        // Arrange
        when(educationRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sut.obtenerPorId(99L));
        assertTrue(ex.getMessage().toLowerCase().contains("recurso no encontrado"));
        verify(educationRepo).findById(99L);
    }
}
