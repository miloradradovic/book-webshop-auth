package com.example.authservice.api;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.client.UserDataResponseDTO;
import com.example.authservice.dto.ModifyUserDTO;
import com.example.authservice.dto.RegisterDataDTO;
import com.example.authservice.dto.UserDTO;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @GetMapping("client/data-for-order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserDataResponseDTO getDataForOrder() {
        UserDataResponse response = userService.getDataForOrder();
        return response.toDTO();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid RegisterDataDTO registerDataDTO) {
        User created = userService.createThrowsException(userMapper.toUser(registerDataDTO));
        return new ResponseEntity<>(userMapper.toUserDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<UserDTO> edit(@RequestBody @Valid ModifyUserDTO modifyUserDTO, @PathVariable int userId) {
        modifyUserDTO.setId(userId);
        User edited = userService.edit(userMapper.toUser(modifyUserDTO));
        return new ResponseEntity<>(userMapper.toUserDTO(edited), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> delete(@PathVariable int userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getById(@PathVariable int userId) {
        User found = userService.getByIdThrowsException(userId);
        return new ResponseEntity<>(userMapper.toUserDTO(found), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(userMapper.toUserDTOList(users), HttpStatus.OK);
    }

    @GetMapping("currently-logged-in")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getCurrentlyLoggedIn() {
        User found = userService.getByCurrentlyLoggedIn();
        return new ResponseEntity<>(userMapper.toUserDTO(found), HttpStatus.OK);
    }

}
