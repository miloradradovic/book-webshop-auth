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
        verify(userRepository).findByEmailAndPassword(email, password);
        verifyNoMoreInteractions(userRepository);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
    }

    @Test
    public void getByEmailAndPasswordReturnsNull() {
        String email = ServiceTestUtils.generateEmail(false);
        String password = ServiceTestUtils.generatePassword(false);

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(null);

        User result = userService.getByEmailAndPassword(email, password);
        verify(userRepository).findByEmailAndPassword(email, password);
        verifyNoMoreInteractions(userRepository);
        assertNull(result);
    }

    @Test
    public void getByEmailSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        User found = ServiceTestUtils.generateUserFoundBy(email, "", 0, "");

        when(userRepository.findByEmail(email)).thenReturn(found);

        User result = userService.getByEmail(email);
        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void getByEmailReturnsNull() {
        String email = ServiceTestUtils.generateEmail(false);

        when(userRepository.findByEmail(email)).thenReturn(null);

        User result = userService.getByEmail(email);
        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
        assertNull(result);
    }

    @Test
    public void getByEmailThrowsExceptionSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        User found = ServiceTestUtils.generateUserFoundBy(email, "", 0, "");

        when(userRepository.findByEmail(email)).thenReturn(found);

        User result = userService.getByEmailThrowsException(email);
        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByEmailThrowsExceptionFail() {
        String email = ServiceTestUtils.generateEmail(false);

        when(userRepository.findByEmail(email)).thenReturn(null);

        userService.getByEmailThrowsException(email);
        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getByEmailOrPhoneNumberSuccess() {
        String email = ServiceTestUtils.generateEmail(true);
        String phone = ServiceTestUtils.generatePhoneNumber(true);
        List<User> found = ServiceTestUtils.generateUserListFoundBy(email, phone);

        when(userRepository.findByEmailOrPhoneNumber(email, phone)).thenReturn(found);

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        verify(userRepository).findByEmailOrPhoneNumber(email, phone);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(found.size(), result.size());
    }

    @Test
    public void getByEmailOrPhoneNumberReturnsEmpty() {
        String email = ServiceTestUtils.generateEmail(false);
        String phone = ServiceTestUtils.generatePhoneNumber(false);

        when(userRepository.findByEmailOrPhoneNumber(email, phone)).thenReturn(new ArrayList<>());

        List<User> result = userService.getByEmailOrPhoneNumber(email, phone);
        verify(userRepository).findByEmailOrPhoneNumber(email, phone);
        verifyNoMoreInteractions(userRepository);
        assertEquals(0, result.size());
    }

    @Test
    public void createUserSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userRepository.save(toRegister)).thenReturn(registered);

        User result = userService.create(toRegister);
        verify(userRepository).save(toRegister);
        verifyNoMoreInteractions(userRepository);
        assertEquals(registered.getId(), result.getId());
    }

    @Test
    public void createAdminSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_ADMIN");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userRepository.save(toRegister)).thenReturn(registered);

        User result = userService.create(toRegister);
        verify(userRepository).save(toRegister);
        verifyNoMoreInteractions(userRepository);
        assertEquals(registered.getId(), result.getId());
    }

    @Test
    public void createThrowsExceptionSuccess() {
        User toRegister = ServiceTestUtils.generateUserToRegister("", "ROLE_USER");
        User registered = ServiceTestUtils.generateRegisteredUser(toRegister);

        when(userRepository.findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(new ArrayList<>());
        when(userRepository.save(toRegister)).thenReturn(registered);

        User result = userService.createThrowsException(toRegister);
        verify(userRepository).findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        verify(userRepository).save(toRegister);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(registered.getId(), result.getId());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmail() {
        User toRegister = ServiceTestUtils.generateUserToRegister("email", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), "");

        when(userRepository.findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
        verify(userRepository).findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("phone", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy("", toRegister.getPhoneNumber());

        when(userRepository.findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
        verify(userRepository).findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createThrowsExceptionFailEmailAndPhoneNumber() {
        User toRegister = ServiceTestUtils.generateUserToRegister("emailandphone", "ROLE_USER");
        List<User> users = ServiceTestUtils.generateUserListFoundBy(toRegister.getEmail(), toRegister.getPhoneNumber());

        when(userRepository.findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber())).thenReturn(users);

        userService.createThrowsException(toRegister);
        verify(userRepository).findByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getDataForOrderSuccess() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        UserDataResponse response = ServiceTestUtils.generateUserDataResponse();
        User found = ServiceTestUtils.generateUserForDataResponse(response);

        when(authService.getCurrentlyLoggedIn()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(found);

        UserDataResponse userDataResponse = userService.getDataForOrder();
        verify(authService).getCurrentlyLoggedIn();
        verify(userRepository).findByEmail(userDetails.getUsername());
        verifyNoMoreInteractions(authService, userRepository);
        assertNotNull(userDataResponse);
        assertEquals(response.getAddress(), userDataResponse.getAddress());
        assertEquals(response.getPhoneNumber(), userDataResponse.getPhoneNumber());
    }

    @Test(expected = UserNotFoundException.class)
    public void getDataForOrderFail() {
        UserDetailsImpl userDetails = ServiceTestUtils.generateUserDetails();
        String invalidEmail = ServiceTestUtils.generateEmail(false);
        userDetails.setUsername(invalidEmail);

        when(authService.getCurrentlyLoggedIn()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(null);

        userService.getDataForOrder();
        verify(authService).getCurrentlyLoggedIn();
        verify(userRepository).findByEmail(userDetails.getUsername());
        verifyNoMoreInteractions(userRepository, authService);
    }

    @Test
    public void getByIdSuccess() {
        int id = ServiceTestUtils.generateUserId(true);
        User found = ServiceTestUtils.generateUserFoundBy("", "", id, "");

        when(userRepository.findById(id)).thenReturn(found);

        User result = userService.getById(id);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int id = ServiceTestUtils.generateUserId(false);

        when(userRepository.findById(id)).thenReturn(null);

        User result = userService.getById(id);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int id = ServiceTestUtils.generateUserId(true);
        User found = ServiceTestUtils.generateUserFoundBy("", "",  id, "");

        when(userRepository.findById(id)).thenReturn(found);

        User result = userService.getByIdThrowsException(id);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int id = ServiceTestUtils.generateUserId(false);

        when(userRepository.findById(id)).thenReturn(null);

        userService.getByIdThrowsException(id);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateUserListSize();
        List<User> users = ServiceTestUtils.generateUserList(listSize);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(listSize, result.size());
    }

    @Test
    public void editSuccess() {
        User toEdit = ServiceTestUtils.generateUserToEdit("");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User edited = ServiceTestUtils.generateEditedUser(foundById, toEdit);

        when(userRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(userRepository.save(foundById)).thenReturn(edited);

        User result = userService.edit(toEdit);
        verify(userRepository).findById(toEdit.getId());
        verify(userRepository).save(foundById);
        verifyNoMoreInteractions(userRepository);
        assertNotNull(result);
        assertEquals(edited.getId(), result.getId());
        assertEquals(edited.getEmail(), result.getEmail());
        assertEquals(edited.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test(expected = UserNotFoundException.class)
    public void editFailId() {
        User toEdit = ServiceTestUtils.generateUserToEdit("id");

        when(userRepository.findById(toEdit.getId())).thenReturn(null);

        userService.edit(toEdit);
        verify(userRepository).findById(toEdit.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailEmail() {
        User toEdit = ServiceTestUtils.generateUserToEdit("email");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User foundByEmail = ServiceTestUtils.generateUserFoundBy(toEdit.getEmail(), "", 0, "");

        when(userRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(userRepository.findByEmail(toEdit.getEmail())).thenReturn(foundByEmail);

        userService.edit(toEdit);
        verify(userRepository).findById(toEdit.getId());
        verify(userRepository).findByEmail(toEdit.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editFailPhoneNumber() {
        User toEdit = ServiceTestUtils.generateUserToEdit("phone");
        User foundById = ServiceTestUtils.generateUserFoundBy("", "", toEdit.getId(), "");
        User foundByPhone = ServiceTestUtils.generateUserFoundBy("", "", 0, toEdit.getPhoneNumber());

        when(userRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(userRepository.findByEmail(toEdit.getEmail())).thenReturn(null);
        when(userRepository.findByPhoneNumber(toEdit.getPhoneNumber())).thenReturn(foundByPhone);

        userService.edit(toEdit);
        verify(userRepository).findById(toEdit.getId());
        verify(userRepository).findByEmail(toEdit.getEmail());
        verify(userRepository).findByPhoneNumber(toEdit.getPhoneNumber());
        verifyNoMoreInteractions(userRepository);
    }
}
