package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;

import java.util.List;

public interface IUserService {

    public ApiResponse saveUser(Users user);

    public ApiResponse deleteUser(String email);

    public ApiResponse disableUserAccount(String email);

    public ApiResponse updateUserInfo(String email, Users user);

    public UserDto getUsers(String email);

    public UserDto getUsers(int id);

    public List<UserDto> getUsers();

    public ApiResponse enableUserAccount(String email);

    public List<UserDto> getAllUser_Disable();
}
