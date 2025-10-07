package com.debtgo.debtgo_backend.service;

import com.debtgo.debtgo_backend.domain.user.User;
import com.debtgo.debtgo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}