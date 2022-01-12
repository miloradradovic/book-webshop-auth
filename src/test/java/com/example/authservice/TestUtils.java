package com.example.authservice;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.*;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return new ModifyUserDTO(-1, "email1@email.com", "dummypassword", "Name", "Surname", "Phone44", "Changed address");
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


    // TODO implement generating methods for tests
}
