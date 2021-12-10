package com.example.authservice.api;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.TokenDataDTO;
import com.example.authservice.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("log-in")
    public ResponseEntity<TokenDataDTO> login(@RequestBody LoginDataDTO loginDataDTO) {
        String token = authService.login(loginDataDTO.toLoginData());
        return new ResponseEntity<>(new TokenDataDTO(loginDataDTO, token), HttpStatus.OK);
    }
}
