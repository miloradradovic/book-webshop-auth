package com.example.authservice.service;

import com.example.authservice.TestUtils;
import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.ModifyUserDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.exceptions.CreateUserFailException;
import com.example.authservice.exceptions.DeleteUserFailException;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.service.impl.AuthService;
import com.example.authservice.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceUnitTests {

    private MockMvc mockMvc;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getByEmailAndPasswordSuccess() {
        String email = TestUtils.generateValidEmail();
        String password = TestUtils.generateValidPassword();
        User found = TestUtils.generateUserFoundByEmailAndPassword(email, password);

        given(userRepository.findByEmailAndPassword(email, password)).willReturn(found);

        User result = userService.getByEmailAndPassword(email, password);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test
    public void getByEmailAndPasswordReturnsNull() {
        String email = TestUtils.generateInvalidEmail();
        String password = TestUtils.generateInvalidPassword();

        given(userRepository.findByEmailAndPassword(email, password)).willReturn(null);

        User result = userService.getByEmailAndPassword(email, password);
        assertNull(result);
    }

    @Test
    public void getByEmailSuccess() {
        String email = TestUtils.generateValidEmail();
        User found = TestUtils.generateUserFoundByEmail(email);

        given(userRepository.findByEmail(email)).willReturn(found);

        User result = userService.getByEmail(email);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test
    public void getByEmailReturnsNull() {
        String email = TestUtils.generateInvalidEmail();

        given(userRepository.findByEmail(email)).willReturn(null);

        User result = userService.getByEmail(email);
        assertNull(result);
    }

    @Test
    public void getByEmailThrowsExceptionSuccess() {
        String email = TestUtils.generateValidEmail();
        User found = TestUtils.generateUserFoundByEmail(email);

        given(userService.getByEmail(email)).willReturn(found);

        User result = userService.getByEmailThrowsException(email);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByEmailThrowsExceptionFail() {
        String email = TestUtils.generateInvalidEmail();

        given(userService.getByEmail(email)).willReturn(null);
        userService.getByEmailThrowsException(email);
    }

    @Test
    public void getByEmailOrPhoneNumberSuccess() {
        String email = TestUtils.generateValidEmail();
        String phone = TestUtils.generateValidPhoneNumber();
        List<User> found = TestUtils.generateFoundUserByEmailOrPhoneList(email, phone);

        given(userRepository.findByEmailOrPhoneNumber(email, phone)).willReturn(found);

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertEquals(found.size(), result.size());
        assertEquals(found.get(0).getEmail(), result.get(0).getEmail());
    }

    @Test
    public void getByEmailOrPhoneNumberReturnsEmpty() {
        String email = TestUtils.generateInvalidEmail();
        String phone = TestUtils.generateInvalidPhoneNumber();

        given(userRepository.findByEmailOrPhoneNumber(email, phone)).willReturn(new ArrayList<>());

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertEquals(0, result.size());
    }

    @Test
    @Transactional
    public void createUserSuccess() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessUser();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toRegister);

        given(userRepository.save(toRegister)).willReturn(registered);

        User created = userService.create(toRegister);
        assertEquals(registered.getEmail(), created.getEmail());
    }

    @Test
    @Transactional
    public void createAdminSuccess() {
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessAdmin();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toRegister);

        given(userRepository.save(toRegister)).willReturn(registered);

        User created = userService.create(toRegister);
        assertEquals(registered.getEmail(), created.getEmail());
    }

    @Test
    @Transactional
    public void createThrowsExceptionSuccess() {
        String email = TestUtils.generateValidEmail();
        String phoneNumber = TestUtils.generateValidPhoneNumber();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOSuccessUser();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        User registered = TestUtils.generateRegisteredUser(toRegister);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(new ArrayList<>());
        given(userService.create(toRegister)).willReturn(registered);

        User result = userService.createThrowsException(toRegister);
        assertEquals(registered.getEmail(), result.getEmail());
    }

    @Test(expected = CreateUserFailException.class)
    public void createThrowsExceptionFailEmail() {
        String email = TestUtils.generateInvalidEmail();
        String phoneNumber = TestUtils.generateValidPhoneNumber();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmail();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> users = TestUtils.generateFoundUserByEmailOrPhoneList(email, phoneNumber);

        given(userRepository.findByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);
        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = CreateUserFailException.class)
    public void createThrowsExceptionFailPhoneNumber() {
        String email = TestUtils.generateValidEmail();
        String phoneNumber = TestUtils.generateInvalidPhoneNumber();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailPhoneNumber();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> users = TestUtils.generateFoundUserByEmailOrPhoneList(email, phoneNumber);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = CreateUserFailException.class)
    public void createThrowsExceptionFailEmailAndPhoneNumber() {
        String email = TestUtils.generateInvalidEmail();
        String phoneNumber = TestUtils.generateInvalidPhoneNumber();
        RegisterDataDTO registerDataDTO = TestUtils.generateRegisterDataDTOFailEmailAndPhoneNumber();
        User toRegister = TestUtils.generateUserToRegister(registerDataDTO);
        List<User> users = TestUtils.generateFoundUserByEmailOrPhoneList(email, phoneNumber);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test
    public void getDataForOrderSuccess() {
        UserDetailsImpl userDetails = TestUtils.generateUserDetailsRoleUser();
        UserDataResponse response = TestUtils.generateUserDataResponse();
        User found = TestUtils.generateUserForDataResponse(response);

        given(userService.getByEmail(userDetails.getUsername())).willReturn(found);
        given(userService.getByEmailThrowsException(userDetails.getUsername())).willReturn(found);
        given(authService.getCurrentlyLoggedIn()).willReturn(userDetails);


        UserDataResponse result = userService.getDataForOrder();
        assertEquals(response.getAddress(), result.getAddress());
        assertEquals(response.getAddress(), result.getAddress());
    }

    @Test(expected = UserNotFoundException.class)
    public void getDataForOrderFail() {
        UserDetailsImpl userDetails = TestUtils.generateUserDetailsRoleUser();
        String invalidEmail = TestUtils.generateInvalidEmail();
        userDetails.setUsername(invalidEmail);
        UserDataResponse response = TestUtils.generateUserDataResponse();


        given(userService.getByEmail(invalidEmail)).willReturn(null);
        given(userService.getByEmailThrowsException(invalidEmail)).willThrow(UserNotFoundException.class);
        given(authService.getCurrentlyLoggedIn()).willReturn(userDetails);

        userService.getDataForOrder();
    }

    @Test
    public void getByIdSuccess() {
        int id = TestUtils.generateUserIdGetBySuccess();
        User found = TestUtils.generateUserFoundById(id);

        given(userRepository.findById(id)).willReturn(found);

        User result = userService.getById(id);
        assertEquals(found.getId(), result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int id = TestUtils.generateUserIdGetByFail();

        given(userRepository.findById(id)).willReturn(null);

        User result = userService.getById(id);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int id = TestUtils.generateUserIdGetBySuccess();
        User found = TestUtils.generateUserFoundById(id);

        given(userService.getById(id)).willReturn(found);

        User result = userService.getByIdThrowsException(id);
        assertEquals(found.getId(), result.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int id = TestUtils.generateUserIdGetByFail();

        given(userService.getById(id)).willReturn(null);

        userService.getByIdThrowsException(id);
    }

    @Test
    public void getAllSuccess() {
        int listSize = TestUtils.generateUserListSize();
        List<User> users = TestUtils.generateUserList(listSize);

        given(userRepository.findAll()).willReturn(users);

        List<User> result = userService.getAll();
        assertEquals(listSize, result.size());
    }

    @Test
    @Transactional
    public void editSuccess() {
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOSuccess();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);
        User foundById = TestUtils.generateUserFoundById(toEdit.getId());
        User edited = TestUtils.generateEditedUser(toEdit);

        given(userRepository.findById(modifyUserDTO.getId())).willReturn(foundById);
        given(userService.getById(modifyUserDTO.getId())).willReturn(foundById);
        given(userService.getByIdThrowsException(modifyUserDTO.getId())).willReturn(foundById);
        given(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).willReturn(null);
        given(userRepository.save(toEdit)).willReturn(edited);

        User result = userService.edit(toEdit);

    }

    @Test(expected = UserNotFoundException.class)
    public void editFailId() {
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailEmail();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);
        int userId = TestUtils.generateUserIdEditFail();
        modifyUserDTO.setId(userId);
        toEdit.setId(userId);

        given(userRepository.findById(modifyUserDTO.getId())).willReturn(null);
        given(userService.getById(modifyUserDTO.getId())).willReturn(null);
        given(userService.getByIdThrowsException(modifyUserDTO.getId())).willThrow(UserNotFoundException.class);

        userService.edit(toEdit);
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void editFailPhoneNumber() {
        ModifyUserDTO modifyUserDTO = TestUtils.generateModifyUserDTOFailPhoneNumber();
        User toEdit = TestUtils.generateUserToEdit(modifyUserDTO);
        User foundById = TestUtils.generateUserFoundById(toEdit.getId());
        User foundByPhone = TestUtils.generateUserFoundByPhone(modifyUserDTO.getPhoneNumber());
        User edited = TestUtils.generateEditedUser(toEdit);

        given(userRepository.findById(modifyUserDTO.getId())).willReturn(foundById);
        given(userService.getById(modifyUserDTO.getId())).willReturn(foundById);
        given(userService.getByIdThrowsException(modifyUserDTO.getId())).willReturn(foundById);
        given(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).willReturn(foundByPhone);

        userService.edit(toEdit);

    }
}
