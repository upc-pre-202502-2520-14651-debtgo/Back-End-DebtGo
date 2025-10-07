package com.debtgo.debtgo_backend.dto;

import com.debtgo.debtgo_backend.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private Role role;
}