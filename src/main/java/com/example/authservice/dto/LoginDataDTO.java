package com.example.authservice.dto;

import com.example.authservice.model.LoginData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDataDTO {

    @NotBlank(message = "Email can't be blank!")
    private String email;

    @NotBlank(message = "Password can't be blank!")
    private String password;

    public LoginData toLoginData() {
        return new LoginData(email, password);
    }
}
