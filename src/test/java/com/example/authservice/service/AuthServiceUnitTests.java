package com.example.authservice.service;

import com.example.authservice.TestUtils;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.exceptions.RefreshTokenFailException;
import com.example.authservice.exceptions.UnauthenticatedException;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.security.jwt.JwtUtils;
import com.example.authservice.service.impl.AuthService;
import com.example.authservice.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceUnitTests {

    private MockMvc mockMvc;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void loginSuccess() {
        LoginData loginData = TestUtils.generateLoginDataSuccess();
        Authentication auth = TestUtils.generateAuthentication(loginData);
        UserDetailsImpl userDetails = TestUtils.generateUserDetailsRoleUser();

        given(authenticationManager.authenticate(auth)).willReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        given(jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))).willReturn("dummytoken");

        String jwt = authService.login(loginData);
        assertNotNull(jwt);
    }

    @Test(expected = UnauthenticatedException.class)
    public void loginFail() {
        LoginData loginData = TestUtils.generateLoginDataBadCredentials();
        Authentication auth = TestUtils.generateAuthentication(loginData);

        given(authenticationManager.authenticate(auth)).willThrow(UnauthenticatedException.class);

        authService.login(loginData);
    }

    @Test
    public void refreshTokenSuccess() {
        String expiredToken = TestUtils.generateJwtToken();
        String newToken = TestUtils.generateNewJwtToken();

        given(jwtUtils.refreshToken(expiredToken)).willReturn(newToken);

        String token = authService.refreshToken(expiredToken);
        assertEquals(newToken, token);
    }

    @Test(expected = RefreshTokenFailException.class)
    public void refreshTokenFail() {
        String expiredToken = TestUtils.generateInvalidJwtToken();

        given(jwtUtils.refreshToken(expiredToken)).willReturn(null);
        authService.refreshToken(expiredToken);
    }

    @Test
    @Transactional
    public void registerSuccess() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessUser();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toRegister);

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(new ArrayList<>());
        given(passwordEncoder.encode(toRegister.getPassword())).willReturn(TestUtils.generateEncodedPassword());
        given(userService.createThrowsException(toRegister)).willReturn(registered);

        User success = authService.register(toRegister);
        assertEquals(registered.getId(), success.getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmail() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmail();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> foundUsers = TestUtils.generateFoundUserList();

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailPhoneNumber() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailPhoneNumber();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> foundUsers = TestUtils.generateFoundUserList();

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmailAndPhoneNumber() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmailAndPhoneNumber();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> foundUsers = TestUtils.generateFoundUserList();

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }
}
