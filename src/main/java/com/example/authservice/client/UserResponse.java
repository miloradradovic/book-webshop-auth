package com.example.authservice.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

    private String email;
    private String password;
    private List<String> roles = new ArrayList<>();

    public UserResponse(String email, String password) {
        this.email = email;
        this.password = password;
        roles.add("ROLE_USER");
    }
}
