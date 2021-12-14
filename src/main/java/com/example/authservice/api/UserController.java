package com.example.authservice.api;

import com.example.authservice.dto.UserResponseDTO;
import com.example.authservice.model.User;
import com.example.authservice.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("get-user-by-username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        User found = userService.findByUsername(username);
        return new ResponseEntity<>(found.toUserResponseDTO(), HttpStatus.OK);
    }
}
