package com.LostAndFound.UserService.controller;

import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody Users user) {
        ApiResponse response = userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUsers(id), HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUsers(email), HttpStatus.OK);
    }

    @PatchMapping("/disableUser/{email}")
    public ResponseEntity<ApiResponse> disableUser(@PathVariable String email) {
        ApiResponse response = userService.disableUserAccount(email);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PatchMapping("/enableUser/{email}")
    public ResponseEntity<ApiResponse> enableUser(@PathVariable String email) {
        ApiResponse response = userService.enableUserAccount(email);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<ApiResponse>  deleteUser(@PathVariable String email)
    {
        ApiResponse response = userService.deleteUser(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateUser/{email}")
    public ResponseEntity<ApiResponse>  updateUser(@PathVariable String email,@RequestBody Users user)
    {
        ApiResponse response = userService.updateUserInfo(email,user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllUser_Disable")
    public ResponseEntity<List<UserDto>>  getAllUser_Disable()
    {
        List<UserDto> list = userService.getAllUser_Disable();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


}
