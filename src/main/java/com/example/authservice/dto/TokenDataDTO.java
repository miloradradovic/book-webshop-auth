package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDataDTO {

    private String email;
    private String accessToken;

    public TokenDataDTO(String token) {
        this.accessToken = token;
    }
}
