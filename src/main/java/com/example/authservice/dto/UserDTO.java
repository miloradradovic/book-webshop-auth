package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    public List<String> roles = new ArrayList<>();

}
