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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceUnitTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void loginSuccess() {
        LoginData loginData = ServiceTestUtils.generateLoginData(true);
        Authentication toAuthenticate = ServiceTestUtils.generateAuthentication(loginData);
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        Authentication authenticated = ServiceTestUtils.generateAuthentication(userDetails);
        String jwtToken = ServiceTestUtils.generateJwtToken(true);

        when(authenticationManager.authenticate(toAuthenticate)).thenReturn(authenticated);
        when(jwtUtils.generateJwtToken(authenticated)).thenReturn(jwtToken);

        String jwtResult = authService.login(loginData);
        assertEquals(jwtToken, jwtResult);
    }

    @Test(expected = UnauthenticatedException.class)
    public void loginFail() {
        LoginData loginData = ServiceTestUtils.generateLoginData(false);
        Authentication auth = ServiceTestUtils.generateAuthentication(loginData);

        when(authenticationManager.authenticate(auth)).thenThrow(UnauthenticatedException.class);

        authService.login(loginData);
    }

    @Test
    public void refreshSuccess() {
        String expiredToken = ServiceTestUtils.generateJwtToken(true);
        String newToken = ServiceTestUtils.generateRefreshedJwtToken();

        when(jwtUtils.refreshToken(expiredToken)).thenReturn(newToken);

        String tokenResult = authService.refreshToken(expiredToken);
        assertEquals(newToken, tokenResult);
    }

    @Test(expected = RefreshTokenFailException.class)
    public void refreshTokenFail() {
        String expiredToken = ServiceTestUtils.generateJwtToken(false);

        when(jwtUtils.refreshToken(expiredToken)).thenReturn(null);

        authService.refreshToken(expiredToken);
    }
    @Test
    @Transactional
    public void registerSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_USER");
        String rawPass = toRegister.getPassword();
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);
        String encodedPass = ServiceTestUtils.generateEncodedPassword();

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(rawPass)).thenReturn(encodedPass);
        when(userService.createThrowsException(toRegister)).thenReturn(registered);

        User result = authService.register(toRegister);
        assertEquals(registered.getId(), result.getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmail() {
        User toRegister = ServiceTestUtils.generateUserToRegister("email", "ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), "");

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(foundUsers);

        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("phone", "ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundBy("", toRegister.getPhoneNumber());

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(foundUsers);

        authService.register(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmailAndPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("emailandphone", "ROLE_USER");
        List<User> foundUsers = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), toRegister.getPhoneNumber());

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(foundUsers);

        authService.register(toRegister);
    }
}
