package com.example.authservice.api;

import com.example.authservice.TestUtils;
import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.*;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.service.impl.AuthService;
import com.example.authservice.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerUnitTests {

    private MockMvc mockMvc;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String basePath = "/api/users";


    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getDataForOrderSuccess() throws Exception {
        loginUser();
        UserDataResponse response = TestUtils.generateUserDataResponse();
        given(userService.getDataForOrder()).willReturn(response);

        mockMvc.perform(get(basePath + "/client/data-for-order")
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(response.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())));
    }

    @Test
    @Transactional
    public void createUserSuccess() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessUser();
        User toCreate = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toCreate);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(userMapper.toUser(registerDataDTO))).willReturn(registered);

        String json = TestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void createAdminSuccess() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessAdmin();
        User toCreate = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toCreate);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(userMapper.toUser(registerDataDTO))).willReturn(registered);

        String json = TestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createFailEmail() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmail();
        User toCreate = TestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFailPhoneNumber() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailPhoneNumber();
        User toCreate = TestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFailEmailAndPhoneNumber() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmailAndPhoneNumber();
        User toCreate = TestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editSuccessRoleAdmin() throws Exception {
        loginAdmin();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOSuccess();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);
        User edited = TestUtils.generateEditedUser(toEdit);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willReturn(edited);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditSuccess())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(edited.getAddress())));
    }

    @Test
    @Transactional
    public void editSuccessRoleUser() throws Exception {
        loginUser();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOSuccess();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);
        User edited = TestUtils.generateEditedUser(toEdit);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willReturn(edited);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditSuccess())
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(edited.getAddress())));
    }

    @Test
    public void editFailEmail() throws Exception {
        loginAdmin();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailEmail();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditSuccess())
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailPhoneNumber() throws Exception {
        loginAdmin();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailPhoneNumber();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditSuccess())
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailEmailAndPhoneNumber() throws Exception {
        loginAdmin();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailEmailAndPhoneNumber();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditSuccess())
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailId() throws Exception {
        loginAdmin();
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailEmailAndPhoneNumber();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserNotFoundException.class);

        String json = TestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + TestUtils.generateUserIdEditFail())
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteSuccess() throws Exception {
        loginAdmin();
        int userId = TestUtils.generateUserIdDeleteSuccess();

        mockMvc.perform(delete(basePath + "/" + userId)).andExpect(status().isOk());
    }

    @Test
    public void deleteFail() throws Exception {
        loginAdmin();
        int userId = TestUtils.generateUserIdDeleteFail();

        mockMvc.perform(delete(basePath + "/" + userId)).andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdSuccess() throws Exception {
        loginAdmin();
        int userId = TestUtils.generateUserIdGetBySuccess();
        User found = TestUtils.generateUserFoundById(userId);
        UserDTO foundDTO = TestUtils.generateUserDTOFoundById(found);

        given(userMapper.toUserDTO(found)).willReturn(foundDTO);
        given(userService.getByIdThrowsException(userId)).willReturn(found);

        mockMvc.perform(get(basePath + "/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(found.getId())));
    }

    @Test
    public void getByIdFail() throws Exception {
        loginAdmin();
        int userId = TestUtils.generateUserIdGetByFail();

        given(userService.getByIdThrowsException(userId)).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(basePath + "/" + userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllSuccess() throws Exception {
        int listSize = TestUtils.generateUserListSize();
        List<User> users = TestUtils.generateUserList(listSize);

        given(userService.getAll()).willReturn(users);

        mockMvc.perform(get(basePath))
                .andExpect(jsonPath("$", hasSize(listSize)))
                .andExpect(status().isOk());
    }


    private void loginUser() {
        UserDetailsImpl user = TestUtils.generateUserDetailsRoleUser();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    private void loginAdmin() {
        UserDetailsImpl user = TestUtils.generateUserDetailsRoleAdmin();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

}
