package com.LostAndFound.UserService.service;

import com.LostAndFound.UserService.commonClasses.ProductDto;
import com.LostAndFound.UserService.dto.PasswordUpdateDto;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;

import java.util.List;

public interface UserService {

    public ApiResponse loginUser(UserDto userDto);

    public ApiResponse unLockUserAccount(String email);

    public ApiResponse updatePassword(PasswordUpdateDto passwordUpdate);

    ApiResponse saveUserAndReportItem(UserDto user, List<ProductDto> products);
}
