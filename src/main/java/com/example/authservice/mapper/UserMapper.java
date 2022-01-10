package com.example.authservice.mapper;

import com.example.authservice.dto.LoginDataDTO;
import com.example.authservice.dto.ModifyUserDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.UserDTO;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    // TODO mapping methods
    public User toUser(ModifyUserDTO modifyUserDTO) {
        return new User(modifyUserDTO.getId(), modifyUserDTO.getEmail(), modifyUserDTO.getPassword(), modifyUserDTO.getName(),
                modifyUserDTO.getSurname(), modifyUserDTO.getPhoneNumber(), modifyUserDTO.getAddress());
    }

    public User toUser(RegisterDataDTO registerDataDTO) {
        return new User(registerDataDTO.getEmail(), registerDataDTO.getPassword(), registerDataDTO.getName(), registerDataDTO.getSurname(),
                registerDataDTO.getPhoneNumber(), registerDataDTO.getAddress(), registerDataDTO.getRoleType());
    }

    public LoginData toLoginData(LoginDataDTO loginDataDTO) {
        return new LoginData(loginDataDTO.getEmail(), loginDataDTO.getPassword());
    }

    public UserDTO toUserDTO(User registered) {
        return new UserDTO(registered.getId(), registered.getEmail(), registered.getPassword(), registered.getName(), registered.getSurname(),
                registered.getPhoneNumber(), registered.getAddress(), registered.getRoles());
    }

    public List<UserDTO> toUserDTOList(List<User> users) {
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toUserDTO(user));
        }
        return dtos;
    }
}
