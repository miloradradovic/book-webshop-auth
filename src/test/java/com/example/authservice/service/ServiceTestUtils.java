package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
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

    public static LoginData generateLoginData(boolean success) {
        if (success) {
            return new LoginData("email1@email.com", "123qweASD");
        }
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

    public static String generateJwtToken(boolean success) {
        if (success) {
            return "valid token";
        }
        return "invalid token";
    }

    public static String generateRefreshedJwtToken() {
        return "refreshed token";
    }

    public static User generateUserToRegister(String success, String role) {
        switch (success) {
            case "":
                return new User("email3@email.com", "Password123", "Name", "Surname", "phone3", "address3", role);
            case "email":
                return new User("email1@email.com", "Password123", "Name", "Surname", "phone3", "address3", role);
            case "phone":
                return new User("email3@email.com", "Password123", "Name", "Surname", "phone1", "address3", role);
            default:  // email and phone
                return new User("email1@email.com", "Password123", "Name", "Surname", "phone1", "address3", role);
        }
    }

    public static User generateRegisteredUser(User toRegister) {
        toRegister.setId(3);
        return toRegister;
    }

    public static String generateEncodedPassword() {
        return "encoded password";
    }

    public static List<User> generateUserListFoundBy(String email, String phone) {
        List<User> users = new ArrayList<>();
        User foundUser = new User();
        if (phone.equals("")) {
            foundUser.setEmail(email);
        } else if (email.equals("")) {
            foundUser.setPhoneNumber(phone);
        } else { // both
            foundUser.setEmail(email);
            foundUser.setPhoneNumber(phone);
        }
        users.add(foundUser);
        return users;
    }

    public static String generateEmail(boolean success) {
        if (success) {
            return "email1@email.com";
        }
        return "invalid@email.com";
    }

    public static String generatePassword(boolean success) {
        if (success) {
            return "$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcrvDTicr7TduP/MTx.Es6";
        }
        return "invalidpassword";
    }

    public static User generateUserFoundBy(String email, String password, int id, String phone) {
        User user = new User();
        if (id != 0) {
            user.setId(id);
        } else if (!email.equals("") && !password.equals("")) {
            user.setEmail(email);
            user.setPassword(password);
        }
        else if (!email.equals("")){
            user.setEmail(email);
        } else { // phone
            user.setPhoneNumber(phone);
        }
        return user;
    }

    public static String generatePhoneNumber(boolean success) {
        if (success) {
            return "phone1";
        }
        return "phoneinvalid";
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

    public static int generateUserId(boolean success) {
        if (success) {
            return 1;
        }
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

    public static User generateUserToEdit(String success) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        switch (success) {
            case "":
                return new User(1, "newemail@email.com", "newpassworD123", "Newname",
                        "Newsurname", "Newphone", "Newaddress", roles);
            case "id":
                return new User(-1, "newemail@email.com", "newpassworD123", "Newname",
                        "Newsurname", "Newphone", "Newaddress", roles);
            case "phone":
                return new User(1, "newemail@email.com", "newpassworD123", "Newname",
                        "Newsurname", "admin", "Newaddress", roles);
            default:  // email
                return new User(1, "admin@admin.com", "newpassworD123", "Newname",
                        "Newsurname", "Newphone", "Newaddress", roles);
        }
    }

    public static User generateEditedUser(User foundById, User toEdit) {
        foundById.setEmail(toEdit.getEmail());
        foundById.setPassword(toEdit.getPassword());
        foundById.setPhoneNumber(toEdit.getPhoneNumber());
        foundById.setAddress(toEdit.getAddress());
        foundById.setSurname(toEdit.getSurname());
        foundById.setName(toEdit.getName());
        return foundById;
    }
}
