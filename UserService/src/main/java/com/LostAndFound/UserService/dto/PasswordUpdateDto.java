package com.LostAndFound.UserService.dto;

public class PasswordUpdateDto {

    private String email;
    private String currentPassword;
    private String newPassword;
    private String reEnterPassword;

    public PasswordUpdateDto(String email, String currentPassword, String newPassword, String reEnterPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.reEnterPassword = reEnterPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {this.currentPassword = currentPassword;}

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
