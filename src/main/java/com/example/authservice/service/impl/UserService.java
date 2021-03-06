package com.example.authservice.service.impl;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.exceptions.CreateUserFailException;
import com.example.authservice.exceptions.DeleteUserFailException;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByEmailThrowsException(String email) {
        User found = getByEmail(email);
        if (found == null) {
            throw new UserNotFoundException();
        }
        return found;
    }

    @Override
    public List<User> getByEmailOrPhoneNumber(String email, String phoneNumber) {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createThrowsException(User user) {
        List<User> found = getByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber());
        if (!found.isEmpty()) {
            throw new UserAlreadyExistsException();
        }
        User created = create(user);
        if (created == null) {
            throw new CreateUserFailException();
        }
        return created;
    }

    @Override
    public UserDataResponse getDataForOrder() {
        UserDetailsImpl currentlyLoggedIn = authService.getCurrentlyLoggedIn();
        User found = getByEmailThrowsException(currentlyLoggedIn.getUsername());
        return new UserDataResponse(found.getAddress(), found.getPhoneNumber());
    }

    @Override
    public User getById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User getByIdThrowsException(int userId) {
        User found = getById(userId);
        if (found == null) {
            throw new UserNotFoundException();
        }
        return found;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User edit(User toEdit) {
        User found = getByIdThrowsException(toEdit.getId());

        if (!toEdit.getEmail().equals(found.getEmail()) && userRepository.findByEmail(toEdit.getEmail()) != null) {
            throw new UserAlreadyExistsException();
        }
        if (!toEdit.getPhoneNumber().equals(found.getPhoneNumber()) &&
                userRepository.findByPhoneNumber(toEdit.getPhoneNumber()) != null) {
            throw new UserAlreadyExistsException();
        }
        found.setAddress(toEdit.getAddress());
        found.setEmail(toEdit.getEmail());
        found.setName(toEdit.getName());
        found.setSurname(toEdit.getSurname());
        found.setPhoneNumber(toEdit.getPhoneNumber());
        found.setRoles(toEdit.getRoles());

        if (!toEdit.getPassword().equals("") &&
                toEdit.getPassword().matches("(?=(.*[0-9]))(?=.*[a-z])(?=(.*[A-Z]))(?=(.*)).{8,}")) {
            found.setPassword(passwordEncoder.encode(toEdit.getPassword()));
        }

        return userRepository.save(found);
    }

    @Override
    public boolean delete(int userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            throw new DeleteUserFailException();
        }
    }

    @Override
    public User getByCurrentlyLoggedIn() {
        return getByEmailThrowsException(authService.getCurrentlyLoggedIn().getUsername());
    }
}
