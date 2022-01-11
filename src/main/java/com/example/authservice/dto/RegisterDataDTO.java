package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDataDTO {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Invalid email!")
    private String email;

    @NotBlank(message = "Password can't be blank!")
    @Size(min = 8, max = 15, message = "Password's length must be at least 8!")
    @Pattern(regexp = "(?=(.*[0-9]))(?=.*[a-z])(?=(.*[A-Z]))(?=(.*)).{8,}", message = "Password must contain at least one uppercase, one lowercase and one digit!")
    private String password;

    @NotBlank(message = "Name can't be blank!")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Name starts with the uppercase!")
    private String name;

    @NotBlank(message = "Surname can't be blank!")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Surname starts with the uppercase!")
    private String surname;

    @NotBlank(message = "Phone number can't be blank!")
    private String phoneNumber;

    @NotBlank(message = "Address can't be blank!")
    private String address;

    @NotBlank(message = "Role can't be blank!")
    private String roleType;
}
