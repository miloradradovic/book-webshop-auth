package com.example.authservice.service;

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
        LoginData loginData = ServiceTestUtils.generateLoginDataSuccess();
        Authentication toAuthenticate = ServiceTestUtils.generateAuthentication(loginData);
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        Authentication authenticated = ServiceTestUtils.generateAuthentication(userDetails);
        String jwtToken = ServiceTestUtils.generateJwtToken();

        given(authenticationManager.authenticate(toAuthenticate)).willReturn(authenticated);
        given(jwtUtils.generateJwtToken(authenticated)).willReturn(jwtToken);

        String jwt = authService.login(loginData);
        assertEquals(jwtToken, jwt);
        assertNotNull(jwt);
    }

    @Test(expected = UnauthenticatedException.class)
    public void loginFail() {
        LoginData loginData = ServiceTestUtils.generateLoginDataBadCredentials();
        Authentication auth = ServiceTestUtils.generateAuthentication(loginData);

        given(authenticationManager.authenticate(auth)).willThrow(UnauthenticatedException.class);

        authService.login(loginData);
    }

    @Test
    public void refreshTokenSuccess() {
        String expiredToken = ServiceTestUtils.generateJwtToken();
        String newToken = ServiceTestUtils.generateNewJwtToken();

        given(jwtUtils.refreshToken(expiredToken)).willReturn(newToken);

        String token = authService.refreshToken(expiredToken);
        assertEquals(newToken, token);
    }

    @Test(expected = RefreshTokenFailException.class)
    public void refreshTokenFail() {
        String expiredToken = ServiceTestUtils.generateInvalidJwtToken();

        given(jwtUtils.refreshToken(expiredToken)).willReturn(null);
        authService.refreshToken(expiredToken);
    }

    @Test
    @Transactional
    public void registerSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegisterSuccess("ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);
        String encodedPass = ServiceTestUtils.generateEncodedPassword(toRegister.getPassword());

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(new ArrayList<>());
        given(passwordEncoder.encode(toRegister.getPassword())).willReturn(encodedPass);
        given(userService.createThrowsException(toRegister)).willReturn(registered);

        User success = authService.register(toRegister);
        assertEquals(registered.getId(), success.getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmail() {
        User toRegister = ServiceTestUtils.generateUserToRegisterFailEmail("ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundByEmail(toRegister.getEmail());

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegisterFailPhoneNumber("ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundByPhoneNumber(toRegister.getPhoneNumber());

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmailAndPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegisterFailEmailAndPhoneNumber("ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundByEmailAndPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());

        given(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).willReturn(foundUsers);
        authService.register(toRegister);
    }
}
