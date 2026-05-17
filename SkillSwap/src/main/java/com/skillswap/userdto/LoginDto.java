package com.skillswap.userdto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank(message = "Please enter the userid")
    private String userid;
    @NotBlank(message = "Pleas enter the password")
    private String password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
