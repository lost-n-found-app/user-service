package com.LostAndFound.UserService.dto;

import jakarta.persistence.Column;

public class UserDto {

    private int userId;

    private String userName;

    private String email;

    private String address;

    private String contact;

    private boolean status;

    public UserDto() {
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public void setStatus(boolean status) {
        this.status = status;
    }
}
