package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.client.UserResponse;
import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.TokenDataDTO;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("log-in")
    public ResponseEntity<TokenDataDTO> login(@RequestBody @Valid LoginDataDTO loginDataDTO) {
        String token = authService.login(loginDataDTO.toLoginData());
        return new ResponseEntity<>(new TokenDataDTO(loginDataDTO, token), HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<TokenDataDTO> refreshToken(@RequestBody TokenDataDTO tokenDataDTO) {
        String token = authService.refreshToken(tokenDataDTO.getAccessToken());
        return new ResponseEntity<>(new TokenDataDTO(token), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDataDTO registerDataDTO) {
        authService.register(registerDataDTO.toEntity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("client/get-user-details")
    public UserResponse getUserDetails() {
        UserDetailsImpl user = authService.getUserDetails();
        return new UserResponse(user.getUsername(), user.getPassword());
    }

    @GetMapping("client/get-current-user-data")
    public UserDataResponse getUserData() {
        return authService.getUserData();
    }
}
