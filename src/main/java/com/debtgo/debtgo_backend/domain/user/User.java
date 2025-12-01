package com.debtgo.debtgo_backend.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false)
    private String role; // ENTREPRENEUR o CONSULTANT

    // Constructor vacío obligatorio para JPA
    public User() {
    }

    // Constructor con 4 parámetros (CORREGIDO)
    public User(String email, String password, String role, String name) {
        this.email = email;
        this.password = password;
        this.role = role; // ✔ ahora sí usa el parámetro correctamente
        this.name = name;
    }

    // Constructor con 3 parámetros (lo que los tests necesitan)
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = "USER"; // ✔ valor por defecto
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}