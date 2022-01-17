package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.exceptions.CreateUserFailException;
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
        String email = ServiceTestUtils.generateValidEmail();
        String password = ServiceTestUtils.generateValidPassword();
        User found = ServiceTestUtils.generateUserFoundByEmailAndPassword(email, password);

        given(userRepository.findByEmailAndPassword(email, password)).willReturn(found);

        User result = userService.getByEmailAndPassword(email, password);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test
    public void getByEmailAndPasswordReturnsNull() {
        String email = ServiceTestUtils.generateInvalidEmail();
        String password = ServiceTestUtils.generateInvalidPassword();

        given(userRepository.findByEmailAndPassword(email, password)).willReturn(null);

        User result = userService.getByEmailAndPassword(email, password);
        assertNull(result);
    }

    @Test
    public void getByEmailSuccess() {
        String email = ServiceTestUtils.generateValidEmail();
        User found = ServiceTestUtils.generateUserFoundByEmail(email);

        given(userRepository.findByEmail(email)).willReturn(found);

        User result = userService.getByEmail(email);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test
    public void getByEmailReturnsNull() {
        String email = ServiceTestUtils.generateInvalidEmail();

        given(userRepository.findByEmail(email)).willReturn(null);

        User result = userService.getByEmail(email);
        assertNull(result);
    }

    @Test
    public void getByEmailThrowsExceptionSuccess() {
        String email = ServiceTestUtils.generateValidEmail();
        User found = ServiceTestUtils.generateUserFoundByEmail(email);

        given(userService.getByEmail(email)).willReturn(found);

        User result = userService.getByEmailThrowsException(email);
        assertEquals(found.getEmail(), result.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByEmailThrowsExceptionFail() {
        String email = ServiceTestUtils.generateInvalidEmail();

        given(userService.getByEmail(email)).willReturn(null);
        userService.getByEmailThrowsException(email);
    }

    @Test
    public void getByEmailOrPhoneNumberSuccess() {
        String email = ServiceTestUtils.generateValidEmail();
        String phone = ServiceTestUtils.generateValidPhoneNumber();
        List<User> found = ServiceTestUtils.generateUserListFoundByEmailOrPhoneNumber(email, phone);

        given(userRepository.findByEmailOrPhoneNumber(email, phone)).willReturn(found);

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertEquals(found.size(), result.size());
        assertEquals(found.get(0).getEmail(), result.get(0).getEmail());
    }

    @Test
    public void getByEmailOrPhoneNumberReturnsEmpty() {
        String email = ServiceTestUtils.generateInvalidEmail();
        String phone = ServiceTestUtils.generateInvalidPhoneNumber();

        given(userRepository.findByEmailOrPhoneNumber(email, phone)).willReturn(new ArrayList<>());

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertEquals(0, result.size());
    }

    @Test
    @Transactional
    public void createUserSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegisterSuccess("ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        given(userRepository.save(toRegister)).willReturn(registered);

        User created = userService.create(toRegister);
        assertEquals(registered.getEmail(), created.getEmail());
    }

    @Test
    @Transactional
    public void createAdminSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegisterSuccess("ROLE_ADMIN");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        given(userRepository.save(toRegister)).willReturn(registered);

        User created = userService.create(toRegister);
        assertEquals(registered.getEmail(), created.getEmail());
    }

    @Test
    @Transactional
    public void createThrowsExceptionSuccess() {
        String email = ServiceTestUtils.generateInvalidEmail();
        String phoneNumber = ServiceTestUtils.generateInvalidPhoneNumber();
        User toRegister = ServiceTestUtils.generateUserToRegisterSuccess("ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(new ArrayList<>());
        given(userService.create(toRegister)).willReturn(registered);

        User result = userService.createThrowsException(toRegister);
        assertEquals(registered.getEmail(), result.getEmail());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmail() {
        String email = ServiceTestUtils.generateValidEmail();
        String phoneNumber = ServiceTestUtils.generateInvalidPhoneNumber();
        User toRegister = ServiceTestUtils.generateUserToRegisterFailEmail("ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundByEmail(email);

        given(userRepository.findByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);
        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = CreateUserFailException.class)
    public void createThrowsExceptionFailPhoneNumber() {
        String email = ServiceTestUtils.generateInvalidEmail();
        String phoneNumber = ServiceTestUtils.generateInvalidPhoneNumber();
        User toRegister = ServiceTestUtils.generateUserToRegisterFailPhoneNumber("ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundByPhoneNumber(phoneNumber);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmailAndPhoneNumber() {
        String email = ServiceTestUtils.generateValidEmail();
        String phoneNumber = ServiceTestUtils.generateValidPhoneNumber();
        User toRegister = ServiceTestUtils.generateUserToRegisterFailEmailAndPhoneNumber("ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundByEmailAndPhoneNumber(email, phoneNumber);

        given(userService.getByEmailOrPhoneNumber(email, phoneNumber)).willReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test
    public void getDataForOrderSuccess() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        UserDataResponse response = ServiceTestUtils.generateUserDataResponse();
        User found = ServiceTestUtils.generateUserForDataResponse(response);

        given(userService.getByEmail(userDetails.getUsername())).willReturn(found);
        given(userService.getByEmailThrowsException(userDetails.getUsername())).willReturn(found);
        given(authService.getCurrentlyLoggedIn()).willReturn(userDetails);


        UserDataResponse result = userService.getDataForOrder();
        assertEquals(response.getAddress(), result.getAddress());
        assertEquals(response.getAddress(), result.getAddress());
    }

    @Test(expected = UserNotFoundException.class)
    public void getDataForOrderFail() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        String invalidEmail = ServiceTestUtils.generateInvalidEmail();
        userDetails.setUsername(invalidEmail);


        given(userService.getByEmail(invalidEmail)).willReturn(null);
        given(userService.getByEmailThrowsException(invalidEmail)).willThrow(UserNotFoundException.class);
        given(authService.getCurrentlyLoggedIn()).willReturn(userDetails);

        userService.getDataForOrder();
    }

    @Test
    public void getByIdSuccess() {
        int id = ServiceTestUtils.generateUserIdGetBySuccess();
        User found = ServiceTestUtils.generateUserFoundById(id);

        given(userRepository.findById(id)).willReturn(found);

        User result = userService.getById(id);
        assertEquals(found.getId(), result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int id = ServiceTestUtils.generateUserIdGetByFail();

        given(userRepository.findById(id)).willReturn(null);

        User result = userService.getById(id);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int id = ServiceTestUtils.generateUserIdGetBySuccess();
        User found = ServiceTestUtils.generateUserFoundById(id);

        given(userService.getById(id)).willReturn(found);

        User result = userService.getByIdThrowsException(id);
        assertEquals(found.getId(), result.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int id = ServiceTestUtils.generateUserIdGetByFail();

        given(userService.getById(id)).willReturn(null);

        userService.getByIdThrowsException(id);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateUserListSize();
        List<User> users = ServiceTestUtils.generateUserList(listSize);

        given(userRepository.findAll()).willReturn(users);

        List<User> result = userService.getAll();
        assertEquals(listSize, result.size());
    }

    @Test
    @Transactional
    public void editSuccess() {
        User toEdit = ServiceTestUtils.generateUserToEdit();
        User foundById = ServiceTestUtils.generateUserFoundById(toEdit.getId());
        User edited = ServiceTestUtils.generateEditedUser(toEdit);

        given(userRepository.findById(toEdit.getId())).willReturn(foundById);
        given(userService.getById(toEdit.getId())).willReturn(foundById);
        given(userService.getByIdThrowsException(toEdit.getId())).willReturn(foundById);
        given(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).willReturn(null);
        given(userRepository.save(toEdit)).willReturn(edited);

        userService.edit(toEdit);

    }

    @Test(expected = UserNotFoundException.class)
    public void editFailId() {
        User toEdit = ServiceTestUtils.generateUserToEditFailId();
        int userId = ServiceTestUtils.generateUserIdEditFail();
        toEdit.setId(userId);

        given(userRepository.findById(userId)).willReturn(null);
        given(userService.getById(userId)).willReturn(null);
        given(userService.getByIdThrowsException(userId)).willThrow(UserNotFoundException.class);

        userService.edit(toEdit);
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void editFailPhoneNumber() {
        String phone = ServiceTestUtils.generatePhoneNumberEditFail();
        User toEdit = ServiceTestUtils.generateUserToEditFailPhoneNumber(phone);
        User foundById = ServiceTestUtils.generateUserFoundById(toEdit.getId());
        User foundByPhone = ServiceTestUtils.generateUserFoundByPhoneNumber(phone);

        given(userRepository.findById(toEdit.getId())).willReturn(foundById);
        given(userService.getById(toEdit.getId())).willReturn(foundById);
        given(userService.getByIdThrowsException(toEdit.getId())).willReturn(foundById);
        given(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).willReturn(foundByPhone);

        userService.edit(toEdit);

    }
}
