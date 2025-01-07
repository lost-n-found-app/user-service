package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;

public interface UserService {

    public ApiResponse saveUser(UserDto userDto);

    public ApiResponse loginUser(UserDto userDto);

    public ApiResponse updatePassword(PasswordUpdateDto passwordUpdate);
}
