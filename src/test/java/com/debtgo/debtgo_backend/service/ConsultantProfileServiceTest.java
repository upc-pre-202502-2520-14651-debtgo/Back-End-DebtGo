package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.profile.ConsultantProfile;
import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.repository.ConsultantProfileRepository;
import com.debtgo.debtgo_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultantProfileServiceTest {

    @Mock
    private ConsultantProfileRepository consultantRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ConsultantProfileService sut;

    private User sampleUser;
    private ConsultantProfile sampleProfile;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(11L);
        sampleUser.setEmail("test@example.com");

        sampleProfile = ConsultantProfile.builder()
                .id(null)
                .name("Juan")
                .skills("Finanzas")
                .rate(40.0)
                .user(null)
                .build();
    }

    @Test
    void createProfile_whenUserExists_setsUserAndSaves() {
        // Arrange
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(consultantRepo.save(any(ConsultantProfile.class))).thenAnswer(inv -> {
            ConsultantProfile p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        // Act
        ConsultantProfile saved = sut.createProfile("test@example.com", sampleProfile);

        // Assert
        assertNotNull(saved);
        assertEquals(99L, saved.getId());
        assertSame(sampleUser, saved.getUser(), "El user debe asignarse al profile antes de guardar");
        verify(userRepo).findByEmail("test@example.com");
        verify(consultantRepo).save(sampleProfile);
    }

    @Test
    void createProfile_whenUserNotFound_throwsRuntimeException() {
        // Arrange
        when(userRepo.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> sut.createProfile("noexiste@example.com", sampleProfile));
        assertTrue(ex.getMessage().toLowerCase().contains("user not found"));
        verify(userRepo).findByEmail("noexiste@example.com");
        verify(consultantRepo, never()).save(any());
    }

    @Test
    void createProfile_verifiesUserSetOnSavedProfile_usingArgumentCaptor() {
        // Arrange
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(consultantRepo.save(any(ConsultantProfile.class))).thenAnswer(inv -> {
            ConsultantProfile p = inv.getArgument(0);
            p.setId(77L);
            return p;
        });

        ArgumentCaptor<ConsultantProfile> captor = ArgumentCaptor.forClass(ConsultantProfile.class);

        // Act
        ConsultantProfile result = sut.createProfile("test@example.com", sampleProfile);

        // Assert
        verify(consultantRepo).save(captor.capture());
        ConsultantProfile savedArg = captor.getValue();
        assertNotNull(savedArg.getUser(), "El profile pasado a save debe contener el user asignado");
        assertEquals(sampleUser.getEmail(), savedArg.getUser().getEmail());
        assertEquals(77L, result.getId());
    }

    @Test
    void getProfileByUser_returnsOptionalFromRepo() {
        // Arrange
        ConsultantProfile stored = ConsultantProfile.builder()
                .id(55L)
                .name("Ana")
                .skills("Legal")
                .rate(60.0)
                .user(sampleUser)
                .build();
        when(consultantRepo.findByUser_Email("test@example.com")).thenReturn(Optional.of(stored));

        // Act
        Optional<ConsultantProfile> opt = sut.getProfileByUser("test@example.com");

        // Assert
        assertTrue(opt.isPresent());
        assertEquals(55L, opt.get().getId());
        verify(consultantRepo).findByUser_Email("test@example.com");
    }

    @Test
    void getProfileByUser_whenNotFound_returnsEmptyOptional() {
        // Arrange
        when(consultantRepo.findByUser_Email("nope@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<ConsultantProfile> opt = sut.getProfileByUser("nope@example.com");

        // Assert
        assertFalse(opt.isPresent());
        verify(consultantRepo).findByUser_Email("nope@example.com");
    }
}
