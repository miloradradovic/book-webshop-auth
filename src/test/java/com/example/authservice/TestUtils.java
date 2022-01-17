package com.example.authservice;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.*;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }


    // ================================
    // CONTROLLER UTILS

    public static LoginDataDTO generateLoginDataDTOSuccess() {
        return new LoginDataDTO("email1@email.com", "123qweASD");
    }

    public static LoginData generateLoginDataSuccess() {
        return new LoginData("email1@email.com", "123qweASD");
    }

    public static String generateJwtToken() {
        return "dummyjwt";
    }

    public static LoginDataDTO generateLoginDataDTOBadCredentials() {
        return new LoginDataDTO("fakeemail", "fakepassword");
    }

    public static LoginData generateLoginDataBadCredentials() {
        return new LoginData("fakeemail", "fakepassword");
    }

    public static RegisterDataDTO generateRegisterDataDTOSuccessUser() {
        return new RegisterDataDTO("email2@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", "ROLE_USER");
    }

    public static User generateUserToRegister(RegisterDataDTO registerDataDTO) {
        return new User(registerDataDTO.getEmail(), registerDataDTO.getPassword(), registerDataDTO.getName(), registerDataDTO.getSurname(), registerDataDTO.getPhoneNumber(), registerDataDTO.getAddress(), registerDataDTO.getRoleType());
    }

    public static User generateRegisteredUser(User toRegister) {
        toRegister.setId(3);
        return toRegister;
    }

    public static RegisterDataDTO generateRegisterDataDTOFailEmail() {
        return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", "ROLE_USER");
    }

    public static RegisterDataDTO generateRegisterDataDTOFailPhoneNumber() {
        return new RegisterDataDTO("email3@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", "ROLE_USER");
    }

    public static RegisterDataDTO generateRegisterDataDTOFailEmailAndPhoneNumber() {
        return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", "ROLE_USER");
    }

    public static UserDataResponse generateUserDataResponse() {
        return new UserDataResponse("address1", "phone1");
    }

    public static UserDetailsImpl generateUserDetailsRoleUser() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return new UserDetailsImpl("email1@email.com", "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static UserDetailsImpl generateUserDetailsRoleAdmin() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        return new UserDetailsImpl("admin@admin.com", "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static RegisterDataDTO generateRegisterDataDTOSuccessAdmin() {
        return new RegisterDataDTO("admin2@admin.com", "Password123", "Admin", "Admin", "admin2", "admin2", "ROLE_ADMIN");
    }

    public static ModifyUserDTO generateModifyUserDTOSuccess() {
        return new ModifyUserDTO(1, "email1@email.com", "dummypassword", "Name", "Surname", "Phone44", "Changed address");
    }

    public static User generateUserToEdit(ModifyUserDTO modifyUserDTO) {
        return new User(modifyUserDTO.getId(), modifyUserDTO.getEmail(), modifyUserDTO.getPassword(), modifyUserDTO.getName(),
                modifyUserDTO.getSurname(), modifyUserDTO.getPhoneNumber(), modifyUserDTO.getAddress());
    }

    public static User generateEditedUser(User toEdit) {
        toEdit.setAddress("Changed address");
        return toEdit;
    }

    public static int generateUserIdEditSuccess() {
        return 1;
    }

    public static int generateUserIdEditFail() {
        return -1;
    }

    public static ModifyUserDTO generateModifyUserDTOFailEmail() {
        return new ModifyUserDTO(-1, "admin@admin.com", "dummypassword", "Name", "Surname", "Phone44", "Changed address");
    }

    public static ModifyUserDTO generateModifyUserDTOFailPhoneNumber() {
        return new ModifyUserDTO(-1, "email1@email.com", "dummypassword", "Name", "Surname", "admin", "Changed address");
    }

    public static ModifyUserDTO generateModifyUserDTOFailEmailAndPhoneNumber() {
        return new ModifyUserDTO(-1, "admin@admin.com", "dummypassword", "Name", "Surname", "admin", "Changed address");
    }

    public static int generateUserIdDeleteSuccess() {
        return 1;
    }

    public static int generateUserIdDeleteFail() {
        return -1;
    }

    public static int generateUserIdGetBySuccess() {
        return 1;
    }

    public static User generateUserFoundById(int userId) {
        User user = new User();
        user.setId(userId);
        user.setPassword("$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcrvDTicr7TduP/MTx.Es6");
        user.setRoles(new ArrayList<>());
        user.getRoles().add("ROLE_USER");
        return user;
    }

    public static UserDTO generateUserDTOFoundById(User found) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(found.getId());
        return userDTO;
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

    public static Authentication generateAuthentication(LoginData loginData) {
        return new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword());
    }

    public static String generateNewJwtToken() {
        return "new valid token";
    }

    public static String generateInvalidJwtToken() {
        return "invalid jwt token";
    }

    public static String generateEncodedPassword() {
        return "encoded password";
    }

    public static List<User> generateFoundUserList() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);
        return users;
    }

    public static String generateValidEmail() {
        return "valid email";
    }

    public static String generateValidPassword() {
        return "valid password";
    }

    public static User generateUserFoundByEmailAndPassword(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public static String generateInvalidEmail() {
        return "invalid email";
    }

    public static String generateInvalidPassword() {
        return "invalid password";
    }

    public static User generateUserFoundByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }

    public static String generateValidPhoneNumber() {
        return "valid phone number";
    }

    public static List<User> generateFoundUserByEmailOrPhoneList(String email, String phone) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setEmail(email);
        user.setPhoneNumber(phone);
        users.add(user);
        return users;
    }

    public static String generateInvalidPhoneNumber() {
        return "invalid phone number";
    }

    public static User generateUserForDataResponse(UserDataResponse response) {
        User user = new User();
        user.setAddress(response.getAddress());
        user.setPhoneNumber(response.getPhoneNumber());
        return user;
    }

    public static User generateUserFoundByPhone(String phoneNumber) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
