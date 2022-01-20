package com.example.authservice.api;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.TokenDataDTO;
import com.example.authservice.dto.UserDTO;
import com.example.authservice.exceptions.RefreshTokenFailException;
import com.example.authservice.exceptions.UnauthenticatedException;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerUnitTests {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void loginSuccess() {
        LoginDataDTO loginDataDTO = ApiTestUtils.generateLoginDataDTO(true);
        LoginData loginData = ApiTestUtils.generateLoginData(loginDataDTO);
        String jwtToken = ApiTestUtils.generateJwtTokenRoleUser();

        when(userMapper.toLoginData(loginDataDTO)).thenReturn(loginData);
        when(authService.login(loginData)).thenReturn(jwtToken);

        ResponseEntity<TokenDataDTO> response = authController.login(loginDataDTO);
        verify(userMapper).toLoginData(loginDataDTO);
        verify(authService).login(loginData);
        verifyNoMoreInteractions(userMapper, authService);
        assertNotNull(response.getBody());
        assertEquals(jwtToken, response.getBody().getAccessToken());
    }

    @Test(expected = UnauthenticatedException.class)
    public void loginBadCredentials() {
        LoginDataDTO loginDataDTO = ApiTestUtils.generateLoginDataDTO(false);
        LoginData loginData = ApiTestUtils.generateLoginData(loginDataDTO);

        when(userMapper.toLoginData(loginDataDTO)).thenReturn(loginData);
        when(authService.login(loginData)).thenThrow(UnauthenticatedException.class);

        authController.login(loginDataDTO);
        verify(userMapper).toLoginData(loginDataDTO);
        verify(authService).login(loginData);
        verifyNoMoreInteractions(userMapper, authService);
    }

    @Test
    public void registerSuccess() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("", "ROLE_USER");
        User toRegister = ApiTestUtils.generateUser(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toRegister);
        UserDTO userDTO = ApiTestUtils.generateUserDTO(registered);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toRegister);
        when(authService.register(toRegister)).thenReturn(registered);
        when(userMapper.toUserDTO(registered)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = authController.register(registerDataDTO);
        verify(userMapper).toUser(registerDataDTO);
        verify(userMapper).toUserDTO(registered);
        verify(authService).register(toRegister);
        verifyNoMoreInteractions(userMapper, authService);
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(registered.getId(), response.getBody().getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmail() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("email", "ROLE_USER");
        User toRegister = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toRegister);
        when(authService.register(toRegister)).thenThrow(UserAlreadyExistsException.class);

        authController.register(registerDataDTO);
        verify(userMapper).toUser(registerDataDTO);
        verify(authService).register(toRegister);
        verifyNoMoreInteractions(userMapper, authService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailPhoneNumber() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("phone", "ROLE_USER");
        User toRegister = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toRegister);
        when(authService.register(toRegister)).thenThrow(UserAlreadyExistsException.class);

        authController.register(registerDataDTO);
        verify(userMapper).toUser(registerDataDTO);
        verify(authService).register(toRegister);
        verifyNoMoreInteractions(userMapper, authService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailEmailAndPhoneNumber() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("emailandphone", "ROLE_USER");
        User toRegister = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toRegister);
        when(authService.register(toRegister)).thenThrow(UserAlreadyExistsException.class);

        authController.register(registerDataDTO);
        verify(userMapper).toUser(registerDataDTO);
        verify(authService).register(toRegister);
        verifyNoMoreInteractions(userMapper, authService);
    }

    @Test
    public void refreshSuccess() {
        TokenDataDTO tokenDataDTO = ApiTestUtils.generateTokenDataDTO(true);
        String refreshedToken = ApiTestUtils.generateRefreshedJwtToken();
        TokenDataDTO result = ApiTestUtils.generateTokenDataDTO(tokenDataDTO.getEmail(), refreshedToken);

        when(authService.refreshToken(tokenDataDTO.getAccessToken())).thenReturn(refreshedToken);

        ResponseEntity<TokenDataDTO> response = authController.refresh(tokenDataDTO);
        verify(authService).refreshToken(tokenDataDTO.getAccessToken());
        verifyNoMoreInteractions(authService);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(result.getAccessToken(), response.getBody().getAccessToken());
    }

    @Test(expected = RefreshTokenFailException.class)
    public void refreshFail() {
        TokenDataDTO tokenDataDTO = ApiTestUtils.generateTokenDataDTO(false);

        when(authService.refreshToken(tokenDataDTO.getAccessToken())).thenThrow(RefreshTokenFailException.class);

        authController.refresh(tokenDataDTO);
        verify(authService).refreshToken(tokenDataDTO.getAccessToken());
        verifyNoMoreInteractions(authService);
    }
}
