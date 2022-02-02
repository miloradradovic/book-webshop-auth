package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.*;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;

import java.util.ArrayList;
import java.util.List;

public class ApiTestUtils {

    public static LoginDataDTO generateLoginDataDTO(boolean success) {
        if (success) {
            return new LoginDataDTO("email1@email.com", "123qweASD");
        }
        return new LoginDataDTO("email1@email.com", "wrong password");
    }

    public static LoginData generateLoginData(LoginDataDTO loginDataDTO) {
        return new LoginData(loginDataDTO.getEmail(), loginDataDTO.getPassword());
    }

    public static String generateJwtTokenRoleUser() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiaWF0IjoxNjQxOTAwNTI4LCJleHAiOjE2NDE5MDIzMjgsInJvbGUiOiJST0xFX1VTRVIifQ.APLX1qDrDXYJ8w8t-j1cVQqCQbqklb2lhPtvGf8uq4CotcJtOABF-RrhASB2Nc6KdBI2lpIx24M54Q4RhYqctg";
    }

    public static RegisterDataDTO generateRegisterDataDTO(String success, String role) {
        switch (success) {
            case "":
                return new RegisterDataDTO("email2@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", role);
            case "email":
                return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", role);
            case "phone":
                return new RegisterDataDTO("email2@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", role);
            default:  // email and phone
                return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", role);
        }
    }

    public static User generateUser(RegisterDataDTO registerDataDTO) {
        return new User(registerDataDTO.getEmail(), registerDataDTO.getPassword(), registerDataDTO.getName(),
                registerDataDTO.getSurname(), registerDataDTO.getPhoneNumber(), registerDataDTO.getAddress(),
                registerDataDTO.getRoleType());
    }

    public static User generateRegisteredUser(User toRegister) {
        toRegister.setId(3);
        return toRegister;
    }

    public static UserDTO generateUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getPhoneNumber(),
                user.getAddress(), user.getRoles());
    }

    public static TokenDataDTO generateTokenDataDTO(boolean success) {
        if (success) {
            return new TokenDataDTO("email1@email.com", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiaWF0IjoxNjQxOTAwNTI4LCJleHAiOjE2NDE5MDIzMjgsInJvbGUiOiJST0xFX1VTRVIifQ.APLX1qDrDXYJ8w8t-j1cVQqCQbqklb2lhPtvGf8uq4CotcJtOABF-RrhASB2Nc6KdBI2lpIx24M54Q4RhYqctg");
        }
        return new TokenDataDTO("invalidemail@email.com", "invalidtoken");
    }

    public static String generateRefreshedJwtToken() {
        return "refreshedtoken";
    }

    public static TokenDataDTO generateTokenDataDTO(String email, String token) {
        return new TokenDataDTO(email, token);
    }

    public static UserDataResponse generateUserDataResponse() {
        return new UserDataResponse("address1", "phone1");
    }

    public static ModifyUserDTO generateModifyUserDTO(String success) {
        switch (success) {
            case "":
                return new ModifyUserDTO(1, "emailnew@email.com", "Dummypassword123", "Name", "Surname", "Changed phone", "Changed address", new ArrayList<>());
            case "id":
                return new ModifyUserDTO(-1, "emailnew@email.com", "Dummypassword123", "Name", "Surname", "Changed phone", "Changed address", new ArrayList<>());
            case "email":
                return new ModifyUserDTO(1, "admin@admin.com", "dummypassword", "Name", "Surname", "phone1", "address1", new ArrayList<>());
            case "phone":
                return new ModifyUserDTO(1, "email1@email.com", "dummypassword", "Name", "Surname", "admin", "address1", new ArrayList<>());
            default:  // email and phone
                return new ModifyUserDTO(1, "admin@admin.com", "dummypassword", "Name", "Surname", "admin", "address1", new ArrayList<>());
        }
    }

    public static User generateUser(ModifyUserDTO modifyUserDTO) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return new User(modifyUserDTO.getId(), modifyUserDTO.getEmail(), modifyUserDTO.getPassword(), modifyUserDTO.getName(),
                modifyUserDTO.getSurname(), modifyUserDTO.getPhoneNumber(), modifyUserDTO.getAddress(), roles);
    }

    public static User generateEditedUser(User toEdit) {
        return new User(toEdit.getId(), toEdit.getEmail(), toEdit.getPassword(), toEdit.getName(), toEdit.getSurname(),
                toEdit.getPhoneNumber(), toEdit.getAddress(), toEdit.getRoles());
    }

    public static int generateUserId(boolean success) {
        if (success) {
            return 1;
        }
        return -1;
    }

    public static User generateUserFoundById(int userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    public static UserDTO generateUserDTOFoundById(int userId) {
        UserDTO dto = new UserDTO();
        dto.setId(userId);
        return dto;
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

    public static List<UserDTO> generateUserDTOList(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            userDTOList.add(dto);
        }
        return userDTOList;
    }
}
