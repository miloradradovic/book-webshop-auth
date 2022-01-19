package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.client.UserDataResponseDTO;
import com.example.authservice.dto.ModifyUserDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.UserDTO;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerUnitTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void getDataForOrderSuccess() {
        UserDataResponse userDataResponse = ApiTestUtils.generateUserDataResponse();
        when(userService.getDataForOrder()).thenReturn(userDataResponse);

        UserDataResponseDTO response = userController.getDataForOrder();
        assertNotNull(response);
        assertEquals(userDataResponse.getAddress(), response.getAddress());
        assertEquals(userDataResponse.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    @Transactional
    public void createUserSuccess() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("", "ROLE_USER");
        User toCreate = ApiTestUtils.generateUser(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toCreate);
        UserDTO userDTO = ApiTestUtils.generateUserDTO(registered);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toCreate);
        when(userService.createThrowsException(toCreate)).thenReturn(registered);
        when(userMapper.toUserDTO(registered)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.create(registerDataDTO);
        verify(userService).createThrowsException(toCreate);
        verifyNoMoreInteractions(userService);
        assertNotNull(response.getBody());
        assertEquals(registered.getId(), response.getBody().getId());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    @Transactional
    public void createAdminSuccess() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("", "ROLE_ADMIN");
        User toCreate = ApiTestUtils.generateUser(registerDataDTO);
        User registered = ApiTestUtils.generateRegisteredUser(toCreate);
        UserDTO userDTO = ApiTestUtils.generateUserDTO(registered);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toCreate);
        when(userService.createThrowsException(toCreate)).thenReturn(registered);
        when(userMapper.toUserDTO(registered)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.create(registerDataDTO);
        verify(userService).createThrowsException(toCreate);
        verifyNoMoreInteractions(userService);
        assertNotNull(response.getBody());
        assertEquals(registered.getId(), response.getBody().getId());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createFailEmail() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("email", "ROLE_USER");
        User toCreate = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toCreate);
        when(userService.createThrowsException(toCreate)).thenThrow(UserAlreadyExistsException.class);

        userController.create(registerDataDTO);
        verify(userService).createThrowsException(toCreate);
        verifyNoMoreInteractions(userService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createFailPhoneNumber() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("phone", "ROLE_USER");
        User toCreate = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toCreate);
        when(userService.createThrowsException(toCreate)).thenThrow(UserAlreadyExistsException.class);

        userController.create(registerDataDTO);
        verify(userService).createThrowsException(toCreate);
        verifyNoMoreInteractions(userService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createFailEmailAndPhoneNumber() {
        RegisterDataDTO registerDataDTO = ApiTestUtils.generateRegisterDataDTO("emailandphone", "ROLE_USER");
        User toCreate = ApiTestUtils.generateUser(registerDataDTO);

        when(userMapper.toUser(registerDataDTO)).thenReturn(toCreate);
        when(userService.createThrowsException(toCreate)).thenThrow(UserAlreadyExistsException.class);

        userController.create(registerDataDTO);
        verify(userService).createThrowsException(toCreate);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @Transactional
    public void editSuccess() {
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTO("");
        User toEdit = ApiTestUtils.generateUser(modifyUserDTO);
        User edited = ApiTestUtils.generateEditedUser(toEdit);
        UserDTO userDTO = ApiTestUtils.generateUserDTO(edited);

        when(userMapper.toUser(modifyUserDTO)).thenReturn(toEdit);
        when(userService.edit(toEdit)).thenReturn(edited);
        when(userMapper.toUserDTO(edited)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.edit(modifyUserDTO, modifyUserDTO.getId());
        verify(userService).edit(toEdit);
        verifyNoMoreInteractions(userService);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailEmail() {
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTO("email");
        User toEdit = ApiTestUtils.generateUser(modifyUserDTO);

        when(userMapper.toUser(modifyUserDTO)).thenReturn(toEdit);
        when(userService.edit(toEdit)).thenThrow(UserAlreadyExistsException.class);

        userController.edit(modifyUserDTO, modifyUserDTO.getId());
        verify(userService).edit(toEdit);
        verifyNoMoreInteractions(userService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailPhoneNumber() {
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTO("phone");
        User toEdit = ApiTestUtils.generateUser(modifyUserDTO);

        when(userMapper.toUser(modifyUserDTO)).thenReturn(toEdit);
        when(userService.edit(toEdit)).thenThrow(UserAlreadyExistsException.class);

        userController.edit(modifyUserDTO, modifyUserDTO.getId());
        verify(userService).edit(toEdit);
        verifyNoMoreInteractions(userService);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailEmailAndPhoneNumber() {
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTO("emailandphone");
        User toEdit = ApiTestUtils.generateUser(modifyUserDTO);

        when(userMapper.toUser(modifyUserDTO)).thenReturn(toEdit);
        when(userService.edit(toEdit)).thenThrow(UserAlreadyExistsException.class);

        userController.edit(modifyUserDTO, modifyUserDTO.getId());
        verify(userService).edit(toEdit);
        verifyNoMoreInteractions(userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void editFailId() {
        ModifyUserDTO modifyUserDTO = ApiTestUtils.generateModifyUserDTO("id");
        User toEdit = ApiTestUtils.generateUser(modifyUserDTO);

        when(userMapper.toUser(modifyUserDTO)).thenReturn(toEdit);
        when(userService.edit(toEdit)).thenThrow(UserNotFoundException.class);

        userController.edit(modifyUserDTO, modifyUserDTO.getId());
        verify(userService).edit(toEdit);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @Transactional
    public void deleteSuccess() {
        int userId = ApiTestUtils.generateUserId(true);

        when(userService.delete(userId)).thenReturn(true);

        ResponseEntity<?> response = userController.delete(userId);
        verify(userService).delete(userId);
        verifyNoMoreInteractions(userService);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteFail() {
        int userId = ApiTestUtils.generateUserId(false);

        when(userService.delete(userId)).thenThrow(UserNotFoundException.class);

        userController.delete(userId);
        verify(userService).delete(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getByIdSuccess() {
        int userId = ApiTestUtils.generateUserId(true);
        User found = ApiTestUtils.generateUserFoundById(userId);
        UserDTO foundDTO = ApiTestUtils.generateUserDTOFoundById(userId);

        when(userService.getByIdThrowsException(userId)).thenReturn(found);
        when(userMapper.toUserDTO(found)).thenReturn(foundDTO);

        ResponseEntity<UserDTO> response = userController.getById(userId);
        verify(userService).getByIdThrowsException(userId);
        verifyNoMoreInteractions(userService);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(foundDTO.getId(), response.getBody().getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByIdFail() {
        int userId = ApiTestUtils.generateUserId(false);

        when(userService.getByIdThrowsException(userId)).thenThrow(UserNotFoundException.class);

        userController.getById(userId);
        verify(userService).getByIdThrowsException(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ApiTestUtils.generateUserListSize();
        List<User> users = ApiTestUtils.generateUserList(listSize);
        List<UserDTO> userDTOList = ApiTestUtils.generateUserDTOList(users);

        when(userService.getAll()).thenReturn(users);
        when(userMapper.toUserDTOList(users)).thenReturn(userDTOList);

        ResponseEntity<List<UserDTO>> response = userController.getAll();
        verify(userService).getAll();
        verifyNoMoreInteractions(userService);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTOList.size(), response.getBody().size());
    }
}
