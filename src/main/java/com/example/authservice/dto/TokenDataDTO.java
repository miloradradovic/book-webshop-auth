package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDataDTO {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Invalid email!")
    private String email;

    @NotBlank(message = "Token can't be blank")
    private String accessToken;

    public TokenDataDTO(String token) {
        this.accessToken = token;
    }
}
