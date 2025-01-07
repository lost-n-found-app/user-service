package com.LostAndFound.UserService.service.service.impl;

import com.LostAndFound.UserService.exceptions.UserAccountTemporaryClosedException;
import com.LostAndFound.UserService.response.ApiResponse;
import com.LostAndFound.UserService.dto.UserDto;
import com.LostAndFound.UserService.entity.Users;
import com.LostAndFound.UserService.exceptions.ResourceNotFoundException;
import com.LostAndFound.UserService.exceptions.UserAlreadyExistsException;
import com.LostAndFound.UserService.repository.IUserRepository;
import com.LostAndFound.UserService.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    IUserRepository userRepo;

    @Override
    public UserDto getUsers(int id) {
        Users users = userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Id"));

        if(!users.isStatus())
            throw  new UserAccountTemporaryClosedException("User Account is Temporary Closed");
        return mapper.map(users, UserDto.class);
    }

    @Override
    public ApiResponse saveUser(Users user) {
        Optional<Users> users = userRepo.findByEmail(user.getEmail());
        if (users.isEmpty()) {
            user.setStatus(true);
            user.setRole("USER");
            userRepo.save(user);
            return new ApiResponse.Builder()
                    .message("User Successfully Added")
                    .statusCode(HttpStatus.CREATED)
                    .success(true).build();
        }
        throw new UserAlreadyExistsException("User Failed to Added [EMAIL SHOULD BE UNIQUE]");
    }

    @Override
    public ApiResponse deleteUser(String email) {
        Users users = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
                userRepo.deleteByEmail(email);
        return new ApiResponse.Builder()
                .message("User Successfully Deleted")
                .statusCode(HttpStatus.OK)
                .success(true).build();
    }

    @Override
    public ApiResponse disableUserAccount(String email)
    {
        Users users = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
        users.setStatus(false);
        userRepo.save(users);
        return new ApiResponse.Builder()
                .message("User Account Disable")
                .statusCode(HttpStatus.OK)
                .success(true).build();
    }

    public ApiResponse enableUserAccount(String email)
    {
        Users users = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
        users.setStatus(true);
        userRepo.save(users);
        return new ApiResponse.Builder()
                .message("User Account Enable")
                .statusCode(HttpStatus.OK)
                .success(true).build();
    }

    @Override
    public List<UserDto> getAllUser_Disable() {
        List<Users> list = userRepo.findAllStatus(false);
        List<UserDto> dtoList = new ArrayList<>();
        for (Users user : list) {
            UserDto userDto = mapper.map(user, UserDto.class);
            dtoList.add(userDto);
        }
        return dtoList;
    }

    @Override
    public ApiResponse updateUserInfo(String email, Users user) {
        Users users = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
        if(user.getUserName()!=null)
        {
            users.setUserName(user.getUserName());
        }
        if(user.getAddress()!=null)
        {
            users.setAddress(user.getAddress());
        }
        if(user.getContact()!=null)
        {
            users.setContact(user.getContact());
        }

        if(user.getPassword()!=null)
        {
            users.setPassword(user.getPassword());
        }
         userRepo.save(users);
        return new ApiResponse.Builder()
                .message("User Info Successfully Updated")
                .statusCode(HttpStatus.OK)
                .success(true).build();
    }

    @Override
    public UserDto getUsers(String email) {
        Users users = userRepo.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("No User Found By This Email"));
        if(!users.isStatus())
            throw  new UserAccountTemporaryClosedException("User Account is Temporary Closed");
        return mapper.map(users, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        List<Users> list = userRepo.findAllStatus(true);
        List<UserDto> dtoList = new ArrayList<>();
        for (Users user : list) {
            UserDto userDto = mapper.map(user, UserDto.class);
            dtoList.add(userDto);
        }
        return dtoList;
    }
}
