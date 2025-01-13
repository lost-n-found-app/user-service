package com.LostAndFound.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDto {

    @NotBlank(message = "Name is mandatory")
    private String userName;
    @Email(message = "Email must be in absolute Format")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,20}$",
            message = "Password must be 8-20 characters long, contain at least one digit, one lowercase letter " +
                    "one uppercase letter, one special character (@#$%^&-+=()), and must not contain spaces.")
    private String password;

    public UserDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
