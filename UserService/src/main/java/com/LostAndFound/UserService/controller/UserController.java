package com.LostAndFound.UserService.controller;

import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody UserDto userDto) {
        ApiResponse response = userService.saveUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/loginUser")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody UserDto userDto) {
        ApiResponse response = userService.loginUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody PasswordUpdateDto passwordUpdate) {
        ApiResponse response = userService.updatePassword(passwordUpdate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
