package com.example.authservice;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.TokenDataDTO;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static RegisterDataDTO generateRegisterDataDTOSuccess() {
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


    // TODO implement generating methods for tests
}
