package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.model.User;

import java.util.List;

public interface IUserService {

    User getByEmailAndPassword(String email, String password);
    User getByEmail(String email);
    User getByEmailThrowsException(String email);
    List<User> getByEmailOrPhoneNumber(String email, String phoneNumber);
    User create(User user);
    User createThrowsException(User user);
    UserDataResponse getDataForOrder();
    User getById(int userId);
    User getByIdThrowsException(int userId);
    List<User> getAll();
    User edit(User toEdit);
    boolean delete(int userId);
    User getByCurrentlyLoggedIn();

}
