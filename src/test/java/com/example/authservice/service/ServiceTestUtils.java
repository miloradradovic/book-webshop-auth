package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTestUtils {


    public static LoginData generateLoginDataSuccess() {
        return new LoginData("email1@email.com", "123qweASD");
    }

    public static LoginData generateLoginDataBadCredentials() {
        return new LoginData("email1@email.com", "wrong password");
    }

    public static Authentication generateAuthentication(LoginData loginData) {
        return new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword());
    }

    public static Authentication generateAuthentication(UserDetailsImpl userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public static UserDetailsImpl generateUserDetails() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return new UserDetailsImpl("email1@email.com", "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static String generateJwtToken() {
        return "dummy token";
    }

    public static String generateNewJwtToken() {
        return "new token";
    }

    public static String generateInvalidJwtToken() {
        return "invalid dummy token";
    }

    public static User generateUserToRegisterSuccess(String role) {
        return new User("email3@email.com", "Password123", "Name", "Surname", "phone3", "address3", role);
    }

    public static User generateUserToRegisterFailEmail(String role) {
        return new User("email1@email.com", "Password123", "Name", "Surname", "phone3", "address3", role);
    }

    public static User generateUserToRegisterFailPhoneNumber(String role) {
        return new User("email3@email.com", "Password123", "Name", "Surname", "phone1", "address3", role);
    }

    public static User generateUserToRegisterFailEmailAndPhoneNumber(String role) {
        return new User("email1@email.com", "Password123", "Name", "Surname", "phone1", "address3", role);
    }

    public static User generateRegisteredUser(User toRegister) {
        toRegister.setId(3);
        return toRegister;
    }

    public static String generateEncodedPassword(String password) {
        password = "encoded password";
        return password;
    }

    public static List<User> generateUserListFoundByEmail(String email) {
        List<User> users = new ArrayList<>();
        User foundUser = new User();
        foundUser.setEmail(email);
        users.add(foundUser);
        return users;
    }

    public static List<User> generateUserListFoundByPhoneNumber(String phoneNumber) {
        List<User> users = new ArrayList<>();
        User foundUser = new User();
        foundUser.setPhoneNumber(phoneNumber);
        users.add(foundUser);
        return users;
    }

    public static List<User> generateUserListFoundByEmailAndPhoneNumber(String email, String phoneNumber) {
        List<User> users = new ArrayList<>();
        User foundUser = new User();
        foundUser.setEmail(email);
        foundUser.setPhoneNumber(phoneNumber);
        users.add(foundUser);
        return users;
    }

    public static List<User> generateUserListFoundByEmailOrPhoneNumber(String email, String phone) {
        List<User> users = new ArrayList<>();
        User foundUser = new User();
        foundUser.setEmail(email);
        foundUser.setPhoneNumber(phone);
        users.add(foundUser);
        return users;
    }

    public static String generateValidEmail() {
        return "email1@email.com";
    }

    public static String generateInvalidEmail() { // email that does not exist
        return "email3@email.com";
    }

    public static String generateValidPassword() {
        return "$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcrvDTicr7TduP/MTx.Es6";
    }

    public static String generateInvalidPassword() {
        return "$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcINVALID7TduP/MTx.Es6";
    }

    public static User generateUserFoundByEmailAndPassword(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public static User generateUserFoundByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }

    public static String generateValidPhoneNumber() {
        return "phone1";
    }

    public static String generateInvalidPhoneNumber() {
        return "phone3";
    }

    public static UserDataResponse generateUserDataResponse() {
        return new UserDataResponse("address1", "phone1");
    }

    public static User generateUserForDataResponse(UserDataResponse response) {
        User user = new User();
        user.setAddress(response.getAddress());
        user.setPhoneNumber(response.getPhoneNumber());
        return user;
    }

    public static int generateUserIdGetBySuccess() {
        return 1;
    }

    public static User generateUserFoundById(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    public static int generateUserIdGetByFail() {
        return -1;
    }

    public static int generateUserListSize() {
        return 2;
    }

    public static List<User> generateUserList(int listSize) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            User user = new User();
            user.setId(i + 1);
            users.add(user);
        }
        return users;
    }

    public static User generateUserToEdit() {
        return new User(1, "email1@email.com", "", "Name", "Surname", "phone44", "address");
        //modifyUserDTO.getId(), modifyUserDTO.getEmail(), modifyUserDTO.getPassword(), modifyUserDTO.getName(),
          //      modifyUserDTO.getSurname(), modifyUserDTO.getPhoneNumber(), modifyUserDTO.getAddress()
    }

    public static User generateEditedUser(User toEdit) {
        return toEdit;
    }

    public static int generateUserIdEditFail() {
        return -1;
    }

    public static User generateUserToEditFailId() {
        User user = new User();
        user.setId(-1);
        return user;
    }

    public static String generatePhoneNumberEditFail() {
        return "admin";
    }

    public static User generateUserToEditFailPhoneNumber(String phone) {
        return new User(1, "email1@email.com", "", "Name", "Surname", phone, "address");
    }

    public static User generateUserFoundByPhoneNumber(String phone) {
        User user = new User();
        user.setPhoneNumber(phone);
        return user;
    }
}
