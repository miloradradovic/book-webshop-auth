package com.example.authservice.api;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.exceptions.UnauthenticatedException;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerUnitTests {

    private MockMvc mockMvc;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String basePath = "/api/auth";


    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void loginSuccess() throws Exception {
        LoginDataDTO loginDataDTO = ApiTestUtils.generateLoginDataDTOSuccess();
        LoginData loginData = ApiTestUtils.generateLoginData(loginDataDTO);
        String jwtToken = ApiTestUtils.generateJwtTokenRoleUser();

        given(userMapper.toLoginData(loginDataDTO)).willReturn(loginData);
        given(authService.login(loginData)).willReturn(jwtToken);
        String json = ApiTestUtils.json(loginDataDTO);

        mockMvc.perform(post(basePath + "/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    public void loginBadCredentials() throws Exception {
        LoginDataDTO loginDataDTO = ApiTestUtils.generateLoginDataDTOBadCredentials();
        LoginData loginData = ApiTestUtils.generateLoginData(loginDataDTO);

        given(userMapper.toLoginData(loginDataDTO)).willReturn(loginData);
        given(authService.login(userMapper.toLoginData(loginDataDTO))).willThrow(UnauthenticatedException.class);
        String json = ApiTestUtils.json(loginDataDTO);

        mockMvc.perform(post(basePath + "/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void registerSuccess() throws Exception {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOSuccess("ROLE_USER");
        User toRegister = ApiTestUtils.generateUserToRegister(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toRegister);

        given(userMapper.toUser(registerDataDTO)).willReturn(toRegister);
        given(authService.register(userMapper.toUser(registerDataDTO))).willReturn(registered);
        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(registered.getId())));
    }

    @Test
    public void registerFailEmail() throws Exception {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailEmail("ROLE_USER");
        User toRegister = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toRegister);
        given(authService.register(userMapper.toUser(registerDataDTO))).willThrow(UserAlreadyExistsException.class);
        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerFailPhoneNumber() throws Exception {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailPhoneNumber("ROLE_USER");
        User toRegister = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toRegister);
        given(authService.register(userMapper.toUser(registerDataDTO))).willThrow(UserAlreadyExistsException.class);
        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerFailEmailAndPhoneNumber() throws Exception {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailEmailAndPhoneNumber("ROLE_USER");
        User toRegister = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toRegister);
        given(authService.register(userMapper.toUser(registerDataDTO))).willThrow(UserAlreadyExistsException.class);
        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
