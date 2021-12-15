package com.example.authservice.dto;

import com.example.authservice.model.LoginData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDataDTO {

    private String email;
    private String password;

    public LoginData toLoginData() {
        return new LoginData(email, password);
    }
}
