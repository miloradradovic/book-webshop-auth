package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceUnitTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Test
    public void getByEmailAndPasswordSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        String password = ServiceTestUtils.generatePassword(true);
        User found = ServiceTestUtils.generateUserFoundBy(email, password, 0, "");

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(found);

        User result = userService.getByEmailAndPassword(email, password);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
    }

    @Test
    public void getByEmailAndPasswordReturnsNull() {
        String email = ServiceTestUtils.generateEmail(false);
        String password = ServiceTestUtils.generatePassword(false);

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(null);

        User result = userService.getByEmailAndPassword(email, password);
        assertNull(result);
    }

    @Test
    public void getByEmailSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        User found = ServiceTestUtils.generateUserFoundBy(email, "", 0, "");

        when(userRepository.findByEmail(email)).thenReturn(found);

        User result = userService.getByEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void getByEmailReturnsNull() {
        String email = ServiceTestUtils.generateEmail(false);

        when(userRepository.findByEmail(email)).thenReturn(null);

        User result = userService.getByEmail(email);
        assertNull(result);
    }

    @Test
    public void getByEmailThrowsExceptionSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        User found = ServiceTestUtils.generateUserFoundBy(email, "", 0, "");

        when(userService.getByEmail(email)).thenReturn(found);

        User result = userService.getByEmailThrowsException(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByEmailThrowsExceptionFail() {
        String email = ServiceTestUtils.generateEmail(false);

        when(userService.getByEmail(email)).thenReturn(null);

        userService.getByEmailThrowsException(email);
    }

    @Test
    public void getByEmailOrPhoneNumberSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        String phone = ServiceTestUtils.generatePhoneNumber(true);
        List<User> found = ServiceTestUtils.generateUserListFoundBy(email, phone);

        when(userRepository.findByEmailOrPhoneNumber(email, phone)).thenReturn(found);

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertNotNull(result);
        assertEquals(found.size(), result.size());
    }

    @Test
    public void getByEmailOrPhoneNumberReturnsEmpty() {
        String email = ServiceTestUtils.generateEmail(false);
        String phone = ServiceTestUtils.generatePhoneNumber(false);

        when(userRepository.findByEmailOrPhoneNumber(email, phone)).thenReturn(new ArrayList<>());

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        assertEquals(0, result.size());
    }

    @Test
    @Transactional
    public void createUserSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userRepository.save(toRegister)).thenReturn(registered);

        User result = userService.create(toRegister);
        assertEquals(registered.getId(), result.getId());
    }

    @Test
    @Transactional
    public void createAdminSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_ADMIN");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userRepository.save(toRegister)).thenReturn(registered);

        User result = userService.create(toRegister);
        assertEquals(registered.getId(), result.getId());
    }

    @Test
    @Transactional
    public void createThrowsExceptionSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(new ArrayList<>());
        when(userService.create(toRegister)).thenReturn(registered);

        User result = userService.createThrowsException(toRegister);
        assertNotNull(result);
        assertEquals(registered.getId(), result.getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmail() {
        User toRegister = ServiceTestUtils.generateUserToRegister("email", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), "");

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("phone", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy("", toRegister.getPhoneNumber());

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmailAndPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("emailandphone", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), toRegister.getPhoneNumber());

        when(userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
    }

    @Test
    public void getDataForOrderSuccess() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        UserDataResponse response = ServiceTestUtils.generateUserDataResponse();
        User found = ServiceTestUtils.generateUserForDataResponse(response);

        when(authService.getCurrentlyLoggedIn()).thenReturn(userDetails);
        when(userService.getByEmail(userDetails.getUsername())).thenReturn(found);
        when(userService.getByEmailThrowsException(userDetails.getUsername())).thenReturn(found);

        UserDataResponse userDataResponse = userService.getDataForOrder();
        assertNotNull(userDataResponse);
        assertEquals(response.getAddress(), userDataResponse.getAddress());
        assertEquals(response.getPhoneNumber(), userDataResponse.getPhoneNumber());
    }

    @Test(expected = UserNotFoundException.class)
    public void getDataForOrderFail() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        String invalidEmail = ServiceTestUtils.generateEmail(false);
        userDetails.setUsername(invalidEmail);

        when(userService.getByEmailThrowsException(userDetails.getUsername())).thenThrow(UserNotFoundException.class);

        userService.getDataForOrder();
    }

    @Test
    public void getByIdSuccess() {
        int id = ServiceTestUtils.generateUserId(true);
        User found = ServiceTestUtils.generateUserFoundBy("", "", id, "");

        when(userRepository.findById(id)).thenReturn(found);

        User result = userService.getById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int id = ServiceTestUtils.generateUserId(false);

        when(userRepository.findById(id)).thenReturn(null);

        User result = userService.getById(id);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int id = ServiceTestUtils.generateUserId(true);
        User found = ServiceTestUtils.generateUserFoundBy("", "",  id, "");

        when(userService.getById(id)).thenReturn(found);

        User result = userService.getByIdThrowsException(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int id = ServiceTestUtils.generateUserId(false);

        when(userService.getById(id)).thenReturn(null);

        userService.getByIdThrowsException(id);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateUserListSize();
        List<User> users = ServiceTestUtils.generateUserList(listSize);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();
        assertNotNull(result);
        assertEquals(listSize, result.size());
    }

    @Test
    @Transactional
    public void editSuccess() {
        User toEdit = ServiceTestUtils.generateUserToEdit("");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User edited = ServiceTestUtils.generateEditedUser(foundById, toEdit);

        when(userService.getById(toEdit.getId())).thenReturn(foundById);
        when(userService.getByIdThrowsException(toEdit.getId())).thenReturn(foundById);
        when(userRepository.save(foundById)).thenReturn(edited);

        User result = userService.edit(toEdit);
        assertNotNull(result);
        assertEquals(edited.getId(), result.getId());
        assertEquals(edited.getEmail(), result.getEmail());
        assertEquals(edited.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test(expected = UserNotFoundException.class)
    public void editFailId() {
        User toEdit = ServiceTestUtils.generateUserToEdit("id");

        when(userService.getByIdThrowsException(toEdit.getId())).thenThrow(UserNotFoundException.class);

        userService.edit(toEdit);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailEmail() {
        User toEdit = ServiceTestUtils.generateUserToEdit("email");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User foundByEmail = ServiceTestUtils.generateUserFoundBy(toEdit.getEmail(), "", 0, "");

        when(userService.getById(toEdit.getId())).thenReturn(foundById);
        when(userService.getByIdThrowsException(toEdit.getId())).thenReturn(foundById);
        when(userRepository.findByEmail(toEdit.getEmail())).thenReturn(foundByEmail);

        userService.edit(toEdit);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailPhoneNumber() {
        User toEdit = ServiceTestUtils.generateUserToEdit("phone");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User foundByPhone = ServiceTestUtils.generateUserFoundBy("", "", 0, toEdit.getPhoneNumber());

        when(userService.getById(toEdit.getId())).thenReturn(foundById);
        when(userService.getByIdThrowsException(toEdit.getId())).thenReturn(foundById);
        when(userRepository.findByEmail(toEdit.getEmail())).thenReturn(null);
        when(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).thenReturn(foundByPhone);

        userService.edit(toEdit);
    }
}
