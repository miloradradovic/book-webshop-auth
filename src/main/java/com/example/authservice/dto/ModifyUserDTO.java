package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifyUserDTO {

    private int id;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Invalid email!")
    private String email;

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

    private List<String> roles = new ArrayList<>();
}
