package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.*;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
        UserDataResponse response = ApiTestUtils.generateUserDataResponse();
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
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOSuccess("ROLE_USER");
        User toCreate = ApiTestUtils.generateUserToRegister(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toCreate);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(userMapper.toUser(registerDataDTO))).willReturn(registered);

        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void createAdminSuccess() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOSuccess("ROLE_ADMIN");
        User toCreate = ApiTestUtils.generateUserToRegister(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toCreate);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(userMapper.toUser(registerDataDTO))).willReturn(registered);

        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createFailEmail() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailEmail("ROLE_USER");
        User toCreate = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFailPhoneNumber() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailPhoneNumber("ROLE_USER");
        User toCreate = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFailEmailAndPhoneNumber() throws Exception {
        loginAdmin();
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTOFailEmailAndPhoneNumber("ROLE_USER");
        User toCreate = ApiTestUtils.generateUserToRegister(registerDataDTO);

        given(userMapper.toUser(registerDataDTO)).willReturn(toCreate);
        given(userService.createThrowsException(toCreate)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(registerDataDTO);

        mockMvc.perform(post(basePath + "/create")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editSuccess() throws Exception {
        loginAdmin();
        int userIdEdit = ApiTestUtils.generateUserIdEditSuccess();
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTOSuccess();
        User toEdit = ApiTestUtils.generateUserToEdit(modifyUserDTO);
        User edited = ApiTestUtils.generateEditedUser(toEdit);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willReturn(edited);

        String json = ApiTestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + userIdEdit)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(edited.getAddress())))
                .andExpect(jsonPath("$.email", is(edited.getEmail())));
    }

    @Test
    public void editFailEmail() throws Exception {
        loginAdmin();
        int userIdEdit = ApiTestUtils.generateUserIdEditSuccess();
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTOFailEmail();
        User toEdit = ApiTestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + userIdEdit)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailPhoneNumber() throws Exception {
        loginAdmin();
        int userIdEdit = ApiTestUtils.generateUserIdEditSuccess();
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTOFailPhoneNumber();
        User toEdit = ApiTestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + userIdEdit)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailEmailAndPhoneNumber() throws Exception {
        loginAdmin();
        int userIdEdit = ApiTestUtils.generateUserIdEditSuccess();
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTOFailEmailAndPhoneNumber();
        User toEdit = ApiTestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserAlreadyExistsException.class);

        String json = ApiTestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + userIdEdit)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailId() throws Exception {
        loginAdmin();
        int userIdEdit = ApiTestUtils.generateUserIdEditFail();
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTOFailEmailAndPhoneNumber();
        User toEdit = ApiTestUtils.generateUserToEdit(modifyUserDTO);

        given(userMapper.toUser(modifyUserDTO)).willReturn(toEdit);
        given(userService.edit(toEdit)).willThrow(UserNotFoundException.class);

        String json = ApiTestUtils.json(modifyUserDTO);

        mockMvc.perform(put(basePath + "/edit/" + userIdEdit)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteSuccess() throws Exception {
        loginAdmin();
        int userId = ApiTestUtils.generateUserIdDeleteSuccess();

        mockMvc.perform(delete(basePath + "/" + userId)).andExpect(status().isOk());
    }

    @Test
    public void deleteFail() throws Exception {
        loginAdmin();
        int userId = ApiTestUtils.generateUserIdDeleteFail();

        mockMvc.perform(delete(basePath + "/" + userId)).andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdSuccess() throws Exception {
        loginAdmin();
        int userId = ApiTestUtils.generateUserIdGetBySuccess();
        User found = ApiTestUtils.generateUserFoundById(userId);
        UserDTO foundDTO = ApiTestUtils.generateUserDTOFoundById(found);

        given(userMapper.toUserDTO(found)).willReturn(foundDTO);
        given(userService.getByIdThrowsException(userId)).willReturn(found);

        mockMvc.perform(get(basePath + "/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(found.getId())));
    }

    @Test
    public void getByIdFail() throws Exception {
        loginAdmin();
        int userId = ApiTestUtils.generateUserIdGetByFail();

        given(userService.getByIdThrowsException(userId)).willThrow(UserNotFoundException.class);

        mockMvc.perform(get(basePath + "/" + userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllSuccess() throws Exception {
        int listSize = ApiTestUtils.generateUserListSize();
        List<User> users = ApiTestUtils.generateUserList(listSize);

        given(userService.getAll()).willReturn(users);

        mockMvc.perform(get(basePath))
                .andExpect(jsonPath("$", hasSize(listSize)))
                .andExpect(status().isOk());
    }


    private void loginUser() {
        UserDetailsImpl user = ApiTestUtils.generateUserDetailsRoleUser();
        SecurityContextHolder.getContext().setAuthentication(ApiTestUtils.generateAuthentication(user));
    }

    private void loginAdmin() {
        UserDetailsImpl user = ApiTestUtils.generateUserDetailsRoleAdmin();
        SecurityContextHolder.getContext().setAuthentication(ApiTestUtils.generateAuthentication(user));
    }

}
