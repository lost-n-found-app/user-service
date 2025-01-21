package com.LostAndFound.UserService.dto;

public class PasswordUpdateDto {

    private String email;
    private String newPassword;
    private String reEnterPassword;
    private String otp;

    public PasswordUpdateDto(String email, String newPassword, String reEnterPassword,String otp) {
        this.email = email;
        this.newPassword = newPassword;
        this.reEnterPassword = reEnterPassword;
        this.otp=otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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
