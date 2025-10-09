package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.repository.UserRepository;
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
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService sut;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("alice@example.com", "secret", "ENTREPRENEUR");
        sampleUser.setId(42L);
    }

    @Test
    void registerUser_savesAndReturnsUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(100L);
            return u;
        });

        User toRegister = new User("bob@example.com", "pwd", "CONSULTANT");

        // Act
        User saved = sut.registerUser(toRegister);

        // Assert
        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals("bob@example.com", saved.getEmail());
        verify(userRepository).save(toRegister);
    }

    @Test
    void loginUser_whenCredentialsMatch_returnsOptionalUser() {
        // Arrange
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleUser));

        // Act
        Optional<User> opt = sut.loginUser("alice@example.com", "secret");

        // Assert
        assertTrue(opt.isPresent());
        assertEquals(42L, opt.get().getId());
        verify(userRepository).findByEmail("alice@example.com");
    }

    @Test
    void loginUser_whenPasswordMismatch_returnsEmptyOptional() {
        // Arrange
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleUser));

        // Act
        Optional<User> opt = sut.loginUser("alice@example.com", "wrong");

        // Assert
        assertFalse(opt.isPresent());
        verify(userRepository).findByEmail("alice@example.com");
    }

    @Test
    void loginUser_whenUserNotFound_returnsEmptyOptional() {
        // Arrange
        when(userRepository.findByEmail("noone@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> opt = sut.loginUser("noone@example.com", "whatever");

        // Assert
        assertFalse(opt.isPresent());
        verify(userRepository).findByEmail("noone@example.com");
    }

    @Test
    void getAllUsers_returnsIterableFromRepository() {
        // Arrange
        User u1 = new User("u1@example.com", "p", "ENTREPRENEUR");
        u1.setId(1L);
        User u2 = new User("u2@example.com", "p", "CONSULTANT");
        u2.setId(2L);
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        // Act
        Iterable<User> all = sut.getAllUsers();

        // Assert
        assertNotNull(all);
        List<User> list = (List<User>) all;
        assertEquals(2, list.size());
        assertEquals(1L, list.get(0).getId());
        verify(userRepository).findAll();
    }
}
