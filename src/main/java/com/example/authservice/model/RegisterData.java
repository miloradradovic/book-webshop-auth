package com.example.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterData {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;

}
