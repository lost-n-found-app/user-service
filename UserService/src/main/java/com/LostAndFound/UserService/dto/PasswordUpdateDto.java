package com.LostAndFound.UserService.dto;

public class PasswordUpdateDto {

    private String email;
    private String newPassword;
    private String reEnterPassword;
    private String token;

    public PasswordUpdateDto(String email, String newPassword, String reEnterPassword,String token) {
        this.email = email;
        this.newPassword = newPassword;
        this.reEnterPassword = reEnterPassword;
        this.token=token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getReEnterPassword() {return reEnterPassword;}

    public void setReEnterPassword(String reEnterPassword) {
        this.reEnterPassword = reEnterPassword;
    }
}
