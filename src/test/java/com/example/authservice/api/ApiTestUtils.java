package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.ModifyUserDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.UserDTO;
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

public class ApiTestUtils {

    public static String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }

    public static LoginDataDTO generateLoginDataDTOSuccess() {
        return new LoginDataDTO("email1@email.com", "123qweASD");
    }

    public static LoginDataDTO generateLoginDataDTOBadCredentials() {
        return new LoginDataDTO("email1@email.com", "wrong password");
    }

    public static LoginData generateLoginData(LoginDataDTO loginDataDTO) {
        return new LoginData(loginDataDTO.getEmail(), loginDataDTO.getPassword());
    }

    public static String generateJwtTokenRoleUser() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbDFAZW1haWwuY29tIiwiaWF0IjoxNjQxOTAwNTI4LCJleHAiOjE2NDE5MDIzMjgsInJvbGUiOiJST0xFX1VTRVIifQ.APLX1qDrDXYJ8w8t-j1cVQqCQbqklb2lhPtvGf8uq4CotcJtOABF-RrhASB2Nc6KdBI2lpIx24M54Q4RhYqctg";
    }

    public static RegisterDataDTO generateRegisterDataDTOSuccess(String role) {
        return new RegisterDataDTO("email2@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", role);
    }

    public static RegisterDataDTO generateRegisterDataDTOFailEmail(String role) {
        return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "Phone2", "Address2", role);
    }

    public static RegisterDataDTO generateRegisterDataDTOFailPhoneNumber(String role) {
        return new RegisterDataDTO("email2@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", role);
    }

    public static RegisterDataDTO generateRegisterDataDTOFailEmailAndPhoneNumber(String role) {
        return new RegisterDataDTO("email1@email.com", "Password123", "Nameee", "Surnameee", "phone1", "Address2", role);
    }

    public static User generateUserToRegister(RegisterDataDTO registerDataDTO) {
        return new User(registerDataDTO.getEmail(), registerDataDTO.getPassword(), registerDataDTO.getName(), registerDataDTO.getSurname(), registerDataDTO.getPhoneNumber(), registerDataDTO.getAddress(), registerDataDTO.getRoleType());
    }

    public static User generateRegisteredUser(User toRegister) {
        toRegister.setId(3); // setting it to 3 bcs there are 2 users in db
        return toRegister;
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

    public static Authentication generateAuthentication(UserDetailsImpl userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public static int generateUserIdEditSuccess() {
        return 1;
    }

    public static int generateUserIdEditFail() {
        return -1;
    }

    public static ModifyUserDTO generateModifyUserDTOSuccess() {
        return new ModifyUserDTO(1, "emailnew@email.com", "dummypassword", "Name", "Surname", "Changed phone", "Changed address");
    }

    public static ModifyUserDTO generateModifyUserDTOFailEmail() {
        return new ModifyUserDTO(1, "admin@admin.com", "dummypassword", "Name", "Surname", "phone1", "address1");
    }

    public static ModifyUserDTO generateModifyUserDTOFailPhoneNumber() {
        return new ModifyUserDTO(1, "email1@email.com", "dummypassword", "Name", "Surname", "admin", "address1");
    }

    public static ModifyUserDTO generateModifyUserDTOFailEmailAndPhoneNumber() {
        return new ModifyUserDTO(1, "admin@admin.com", "dummypassword", "Name", "Surname", "admin", "address1");
    }

    public static User generateUserToEdit(ModifyUserDTO modifyUserDTO) {
        return new User(modifyUserDTO.getId(), modifyUserDTO.getEmail(), modifyUserDTO.getPassword(), modifyUserDTO.getName(),
                modifyUserDTO.getSurname(), modifyUserDTO.getPhoneNumber(), modifyUserDTO.getAddress());
    }

    public static User generateEditedUser(User toEdit) {
        return toEdit;
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

    public static int generateUserIdGetByFail() {
        return -1;
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


    /*
    public static Authentication generateAuthentication(LoginData loginData) {
        return new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword());
    }

    public static UserDetailsImpl generateUserDetails(LoginData loginData, String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);
        return new UserDetailsImpl(loginData.getEmail(), "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static Authentication generateAuthentication(UserDetailsImpl userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
     */
}
