package com.example.authservice.api;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.TokenDataDTO;
import com.example.authservice.dto.UserDTO;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.TokenData;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserMapper userMapper;

    @PostMapping("log-in")
    public ResponseEntity<TokenDataDTO> login(@RequestBody @Valid LoginDataDTO loginDataDTO) {
        TokenData tokens = authService.login(userMapper.toLoginData(loginDataDTO));
        return new ResponseEntity<>(new TokenDataDTO(loginDataDTO.getEmail(), tokens.getAccessToken(), tokens.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<TokenDataDTO> refresh(@RequestBody TokenDataDTO tokenDataDTO) {
        TokenData token = authService.refreshToken(tokenDataDTO.getRefreshToken());
        return new ResponseEntity<>(new TokenDataDTO(tokenDataDTO.getEmail(), token.getAccessToken(), token.getRefreshToken()), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDataDTO registerDataDTO) {
        User registered = authService.register(userMapper.toUser(registerDataDTO));
        return new ResponseEntity<>(userMapper.toUserDTO(registered), HttpStatus.CREATED);
    }
}
